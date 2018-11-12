package com.challenge.pedrotorres.pacificbooking.services.booking.impl;

import com.challenge.pedrotorres.pacificbooking.commons.exceptions.ValidationException;
import com.challenge.pedrotorres.pacificbooking.api.responses.booking.BookingInfo;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.CancelBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.ChangeBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.commons.generators.BookingCodeGenerator;
import com.challenge.pedrotorres.pacificbooking.api.responses.Response;
import com.challenge.pedrotorres.pacificbooking.commons.helpers.ValidatorHelper;
import com.challenge.pedrotorres.pacificbooking.domain.booking.Booking;
import com.challenge.pedrotorres.pacificbooking.domain.camper.Camper;
import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import com.challenge.pedrotorres.pacificbooking.domain.campsite.SiteAvailability;
import com.challenge.pedrotorres.pacificbooking.repositories.booking.BookingRepository;
import com.challenge.pedrotorres.pacificbooking.repositories.camper.CamperRepository;
import com.challenge.pedrotorres.pacificbooking.repositories.campsite.SiteAvailabilityRepository;
import com.challenge.pedrotorres.pacificbooking.repositories.campsite.SiteRepository;
import com.challenge.pedrotorres.pacificbooking.services.booking.BookingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.*;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private SiteAvailabilityRepository siteAvailabilityRepository;

    private BookingRepository bookingRepository;

    private SiteRepository siteRepository;

    private CamperRepository camperRepository;

    @Autowired
    public BookingServiceImpl(SiteAvailabilityRepository siteAvailabilityRepository, BookingRepository bookingRepository, SiteRepository siteRepository, CamperRepository camperRepository) {
        this.siteAvailabilityRepository = siteAvailabilityRepository;
        this.bookingRepository = bookingRepository;
        this.siteRepository = siteRepository;
        this.camperRepository = camperRepository;
    }

    @Override
    public Response getBooking(String bookingCode) {
        Response.ResponseBuilder responseBuilder = new Response.ResponseBuilder();

        try {
            Booking booking = this.getValidBookingByCode(bookingCode);

            responseBuilder.results(BookingInfo.mapFromBooking(booking)).withStatus(HTTP_OK);
        } catch (ValidationException e) {
            responseBuilder.addError(e.getMessage()).withStatus(HTTP_NOT_FOUND);
        }
        return responseBuilder.build();
    }

    @Override
    public Response addBooking(NewBookingRequest request) {
        Response.ResponseBuilder responseBuilder = new Response.ResponseBuilder();

        try{
            Site site = this.getValidSiteById(request.getCampsiteId());

            responseBuilder.results(this.createBooking(request, site)).withStatus(HTTP_CREATED);
        } catch (ValidationException e) {
            responseBuilder.addError(e.getMessage()).withStatus(e.getStatus() == null ? HTTP_BAD_REQUEST : e.getStatus());
        }

        return responseBuilder.build();
    }

    @Override
    public Response changeBooking(ChangeBookingRequest request) {
        Response.ResponseBuilder responseBuilder = new Response.ResponseBuilder();

        try {
            responseBuilder.results(this.updateBooking(request)).withStatus(HTTP_OK);

        } catch (ValidationException e) {
            responseBuilder.addError(e.getMessage()).withStatus(HTTP_BAD_REQUEST);
        }
        return responseBuilder.build();
    }

    @Override
    public Response cancelBooking(CancelBookingRequest request) {
        Response.ResponseBuilder responseBuilder = new Response.ResponseBuilder();

        try {
            Booking booking = this.getValidBookingByCode(request.getBookingCode());
            this.increaseRemainingPlaces(booking);
            this.cancel(booking);
            responseBuilder.withStatus(HTTP_OK);

        } catch (ValidationException e) {
            responseBuilder.addError(e.getMessage()).withStatus(e.getStatus());
        }
        return responseBuilder.build();
    }

    private BookingInfo createBooking(NewBookingRequest request, Site site) {
        LocalDate startDate = this.getDate(request.getStartDate());
        LocalDate endDate = this.getDate(request.getEndDate());

        this.validateBooking(startDate, endDate, request.getFirstName(), request.getLastName(), request.getEmail());
        this.decreaseAvailableRemainingPlaces(this.getValidAvailabilities(request.getCampsiteId(), startDate, endDate));

        Booking booking = new Booking();
        booking.setSite(site);
        booking.setCamper(this.createCamper(request));
        booking.setCode(BookingCodeGenerator.getBookingCode());
        booking.setCreated(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setStartDate(LocalDate.parse(request.getStartDate()));
        booking.setEndDate(LocalDate.parse(request.getEndDate()));
        bookingRepository.save(booking);

        return BookingInfo.mapFromBooking(booking);
    }

    private BookingInfo updateBooking(ChangeBookingRequest request) {
        LocalDate startDate = this.getDate(request.getStartDate());
        LocalDate endDate = this.getDate(request.getEndDate());
        this.validateBooking(startDate, endDate, request.getFirstName(), request.getLastName(), request.getEmail());

        Booking booking = this.getValidBookingByCode(request.getBookingCode());

        this.increaseRemainingPlaces(booking);
        this.decreaseAvailableRemainingPlaces(this.getValidAvailabilities(request.getCampsiteId(), startDate, endDate));

        booking.setCreated(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setStartDate(LocalDate.parse(request.getStartDate()));
        booking.setEndDate(LocalDate.parse(request.getEndDate()));
        booking.setModified(LocalDateTime.now());

        Camper camper = booking.getCamper();
        camper.setLastName(request.getFirstName());
        camper.setLastName(request.getLastName());
        camper.setEmail(request.getEmail());
        camperRepository.save(camper);

        bookingRepository.save(booking);

        return BookingInfo.mapFromBooking(booking);
    }

    private void validateBooking(LocalDate startDate, LocalDate endDate, String firstName, String lastName, String email) {
        this.validateFirstName(firstName);
        this.validateLastName(lastName);
        this.validateEmail(email);
        this.validateStartDateGreaterThanEndDate(startDate, endDate);
        this.validateStartDateAfterToday(startDate);
        this.validateEndDateOverOneMonth(endDate);
        this.validateMaxDaysBooking(startDate, endDate);
        this.validateMinDaysBooking(startDate, endDate);
    }

    private List<SiteAvailability> getValidAvailabilities(Long siteId, LocalDate startDate, LocalDate endDate) {
        List<SiteAvailability> availabilities = siteAvailabilityRepository.getAvailabilitiesWithPlacesByDateAndSite(siteId, startDate, endDate);
        long numberOfDays = ValidatorHelper.daysWithinPeriod(startDate, endDate);

        if (numberOfDays > availabilities.size()) {
            throw new ValidationException("The requested period is no longer available.");
        }
        return availabilities;
    }

    private void decreaseAvailableRemainingPlaces(List<SiteAvailability> availabilities) {
        availabilities.forEach(day -> day.setRemainingPlaces(day.getRemainingPlaces() - 1));
        siteAvailabilityRepository.saveAll(availabilities);
    }

    private Camper createCamper(NewBookingRequest request) {
        Camper camper = new Camper();
        camper.setEmail(request.getEmail());
        camper.setFirstName(request.getFirstName());
        camper.setLastName(request.getLastName());
        camperRepository.save(camper);
        return camper;
    }

    private void cancel(Booking booking) {
        if(Booking.BookingStatus.CANCELLED == booking.getStatus()) {
            throw new ValidationException("The booking " + booking.getCode() + " is already cancelled", HTTP_BAD_REQUEST);
        } else {
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            booking.setModified(LocalDateTime.now());
            bookingRepository.save(booking);
        }
    }

    private void increaseRemainingPlaces(Booking booking) {
        List<SiteAvailability> availabilities = siteAvailabilityRepository.getAvailabilitiesByDateAndSite(booking.getSite().getId(), booking.getStartDate(), booking.getEndDate());
        availabilities.forEach(day -> day.setRemainingPlaces(day.getRemainingPlaces() + 1));
        siteAvailabilityRepository.saveAll(availabilities);
    }

    private Booking getValidBookingByCode(String bookingByCode) {
        Booking booking = bookingRepository.getBookingByCode(bookingByCode);

        if(booking == null) {
            throw new ValidationException("The booking " + bookingByCode + " was not found.", HTTP_NOT_FOUND);
        }

        return booking;
    }

    private Site getValidSiteById(Long campsiteId) {
        Optional<Site> site = siteRepository.findById(campsiteId);

        if(!site.isPresent()) {
            throw new ValidationException("A campsite need to be informed in order to make a new booking.");
        }
        return site.get();
    }

    private LocalDate getDate(String strDate) {
        if(!ValidatorHelper.isValidLocalDate(strDate)){
            throw new ValidationException("Date " + strDate + " is not in a valid format yyyy-MM-dd.");
        }
        return LocalDate.parse(strDate);
    }

    private void validateFirstName(String firstName) {
        if (StringUtils.isBlank(firstName)) {
            throw new ValidationException("First name is required.");
        }
    }

    private void validateLastName(String lastName) {
        if (StringUtils.isBlank(lastName)) {
            throw new ValidationException("Last name is required.");
        }
    }

    private void validateEmail(String email) {
        if (!ValidatorHelper.isValidEmail(email)) {
            throw new ValidationException("The email informed is not valid.");
        }
    }

    private void validateStartDateGreaterThanEndDate(LocalDate startDate, LocalDate endDate) {
        if (ValidatorHelper.isStartDateGreaterThanEndDate(startDate, endDate)) {
            throw new ValidationException("Start date cannot be greater than the end date.");
        }
    }

    private void validateEndDateOverOneMonth(LocalDate endDate) {
        if (ValidatorHelper.isPeriodOverOneMonth(LocalDate.now(), endDate)) {
            throw new ValidationException("The end date exceed over a month.");
        }
    }

    private void validateStartDateAfterToday(LocalDate startDate) {
        if (!ValidatorHelper.isDateAfterToday(startDate)) {
            throw new ValidationException("Start date after the current date.");
        }
    }

    private void validateMaxDaysBooking(LocalDate startDate, LocalDate endDate) {
        if(ValidatorHelper.daysWithinPeriod(startDate, endDate) > 3 ){
            throw new ValidationException("Bookings are only available up to a maximum of 3 days.");
        }
    }

    private void validateMinDaysBooking(LocalDate startDate, LocalDate endDate) {
        if(ValidatorHelper.daysWithinPeriod(startDate, endDate) < 1 ){
            throw new ValidationException("Bookings are required at least 1 day of stay.");
        }
    }
}
