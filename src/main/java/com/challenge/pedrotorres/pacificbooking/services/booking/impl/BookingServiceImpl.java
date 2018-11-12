package com.challenge.pedrotorres.pacificbooking.services.booking.impl;

import com.challenge.pedrotorres.pacificbooking.api.requests.booking.BookingConfirmation;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.CancelBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.ChangeBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.commons.BookingCodeGenerator;
import com.challenge.pedrotorres.pacificbooking.commons.Response;
import com.challenge.pedrotorres.pacificbooking.domain.booking.Booking;
import com.challenge.pedrotorres.pacificbooking.domain.camper.Camper;
import com.challenge.pedrotorres.pacificbooking.domain.campsite.SiteAvailability;
import com.challenge.pedrotorres.pacificbooking.repositories.booking.BookingRepository;
import com.challenge.pedrotorres.pacificbooking.repositories.camper.CamperRepository;
import com.challenge.pedrotorres.pacificbooking.repositories.campsite.SiteAvailabilityRepository;
import com.challenge.pedrotorres.pacificbooking.repositories.campsite.SiteRepository;
import com.challenge.pedrotorres.pacificbooking.services.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private Response.ResponseBuilder responseBuilder;

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
    public Response addBooking(NewBookingRequest request) {
        responseBuilder = new Response.ResponseBuilder();

        Booking booking = new Booking();

        booking.setSite(siteRepository.findById(request.getSiteId()).get());

        booking.setCamper(new Camper());
        booking.getCamper().setEmail(request.getEmail());
        booking.getCamper().setFirstName(request.getFirstName());
        booking.getCamper().setLastName(request.getLastName());

        booking.setCode(BookingCodeGenerator.getBookingCode());

        booking.setCreated(LocalDateTime.now());

        booking.setStartDate(LocalDate.parse(request.getStartDate()));
        booking.setEndDate(LocalDate.parse(request.getEndDate()));

        List<SiteAvailability> availabilities = siteAvailabilityRepository.getAvailabilitiesByDateAndSite(request.getSiteId(), LocalDate.parse(request.getStartDate()), LocalDate.parse(request.getEndDate()));

        Long numberOfDays = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());

        if (numberOfDays > availabilities.size()){
            responseBuilder.addError("The requested period is no longer available.");

        }else {
            availabilities.forEach(day -> day.setRemainingPlaces(day.getRemainingPlaces() - 1));
            siteAvailabilityRepository.saveAll(availabilities);
            camperRepository.save(booking.getCamper());
            bookingRepository.save(booking);

            BookingConfirmation confirmation = new BookingConfirmation();

            confirmation.setBookingCode(booking.getCode());
            confirmation.setBookingId(booking.getId());
            confirmation.setCamperFullName(booking.getCamper().getFirstName() + " " + booking.getCamper().getLastName());

            responseBuilder.results(confirmation);
        }
        return responseBuilder.build();
    }

    @Override
    public Response changeBooking(ChangeBookingRequest request) {
        return null;
    }

    @Override
    public Response cancelBooking(CancelBookingRequest request) {
        return null;
    }
}
