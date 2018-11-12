package com.challenge.pedrotorres.pacificbooking.services.campsite.impl;

import com.challenge.pedrotorres.pacificbooking.api.requests.campsite.AvailabilityRequest;
import com.challenge.pedrotorres.pacificbooking.api.responses.Response;
import com.challenge.pedrotorres.pacificbooking.commons.exceptions.ValidationException;
import com.challenge.pedrotorres.pacificbooking.commons.helpers.ValidatorHelper;
import com.challenge.pedrotorres.pacificbooking.domain.campsite.SiteAvailability;
import com.challenge.pedrotorres.pacificbooking.repositories.campsite.SiteAvailabilityRepository;
import com.challenge.pedrotorres.pacificbooking.services.campsite.SiteAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.challenge.pedrotorres.pacificbooking.api.responses.Response.ResponseBuilder;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;


@Service
public class SiteAvailabilityServiceImpl implements SiteAvailabilityService {

    private SiteAvailabilityRepository siteAvailabilityRepository;

    @Autowired
    public SiteAvailabilityServiceImpl(SiteAvailabilityRepository siteAvailabilityRepository) {
        this.siteAvailabilityRepository = siteAvailabilityRepository;
    }

    @Override
    public Response getAvailabilitiesDatesByDateAndSite(AvailabilityRequest request) {
        ResponseBuilder responseBuilder = new ResponseBuilder();

        try {
            Long siteId = request.getSiteId();
            LocalDate startDate = this.getStartDate(request.getStartDate());;
            LocalDate endDate = this.getEndDate(request.getEndDate());

            this.validateSiteId(siteId);
            this.validateStartDateGreaterThanEndDate(startDate, endDate);
            this.validatePeriodOverOneMonth(startDate, endDate);
            this.validateStartDateBeforeToday(startDate);

            List<String> availabilities = siteAvailabilityRepository.getAvailabilitiesWithPlacesByDateAndSite(siteId, startDate, endDate)
                    .stream()
                    .map(SiteAvailability::getDayDate)
                    .map(LocalDate::toString)
                    .collect(Collectors.toList());

            responseBuilder.results(availabilities).withStatus(HTTP_OK);
        } catch (ValidationException e) {
            responseBuilder.addError(e.getMessage()).withStatus(HTTP_BAD_REQUEST);
        }
        return responseBuilder.build();
    }

    private LocalDate getStartDate(String strStartDate) {
        if (strStartDate == null) {
            return LocalDate.now();
        } else if(!ValidatorHelper.isValidLocalDate(strStartDate)){
            throw new ValidationException("Start date is not in a valid format yyyy-MM-dd.");
        }
        return LocalDate.parse(strStartDate);
    }

    private LocalDate getEndDate(String strEndDate) {
        if (strEndDate == null) {
            return LocalDate.now().plusMonths(1);
        } else if(!ValidatorHelper.isValidLocalDate(strEndDate)){
            throw new ValidationException("End date is not in a valid format yyyy-MM-dd.");
        }
        return LocalDate.parse(strEndDate);
    }

    private void validateSiteId(Long siteId) {
        if (siteId == null){
            throw new ValidationException("You need to inform the campsite in order to consult the available dates.");
        }
    }

    private void validateStartDateGreaterThanEndDate(LocalDate startDate, LocalDate endDate) {
        if (ValidatorHelper.isStartDateGreaterThanEndDate(startDate, endDate)) {
            throw new ValidationException("Start date cannot be greater than the end date.");
        }
    }

    private void validatePeriodOverOneMonth(LocalDate startDate, LocalDate endDate) {
        if (ValidatorHelper.isPeriodOverOneMonth(startDate, endDate)) {
            throw new ValidationException("The period between start date and end date exceed over a month.");
        }
    }

    private void validateStartDateBeforeToday(LocalDate startDate) {
        if (ValidatorHelper.isDateBeforeToday(startDate)) {
            throw new ValidationException("Start date cannot be smaller than the current date.");
        }
    }
}
