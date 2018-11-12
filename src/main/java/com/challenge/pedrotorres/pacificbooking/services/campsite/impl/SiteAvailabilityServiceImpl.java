package com.challenge.pedrotorres.pacificbooking.services.campsite.impl;

import com.challenge.pedrotorres.pacificbooking.api.requests.campsite.AvailabilityRequest;
import com.challenge.pedrotorres.pacificbooking.commons.Response;
import com.challenge.pedrotorres.pacificbooking.commons.ValidatorHelper;
import com.challenge.pedrotorres.pacificbooking.domain.campsite.SiteAvailability;
import com.challenge.pedrotorres.pacificbooking.repositories.campsite.SiteAvailabilityRepository;
import com.challenge.pedrotorres.pacificbooking.services.campsite.SiteAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.challenge.pedrotorres.pacificbooking.commons.Response.ResponseBuilder;


@Service
public class SiteAvailabilityServiceImpl implements SiteAvailabilityService {

    private SiteAvailabilityRepository siteAvailabilityRepository;

    private ResponseBuilder responseBuilder;

    @Autowired
    public SiteAvailabilityServiceImpl(SiteAvailabilityRepository siteAvailabilityRepository) {
        this.siteAvailabilityRepository = siteAvailabilityRepository;
    }

    @Override
    public Response getAvailabilitiesDatesByDateAndSite(AvailabilityRequest request) {
        responseBuilder = new ResponseBuilder();

        Long siteId = null;
        LocalDate startDate = this.getStartDate(request.getStartDate());;
        LocalDate endDate = this.getEndDate(request.getEndDate());

        if (request.getSite() == null || request.getSite().getId() == null){
            responseBuilder.addError("You need to inform the campsite in order to consult the available dates.");
        } else {
            siteId = request.getSite().getId();
        }

        this.validateStartDateGreaterThanEndDate(startDate, endDate);
        this.validatePeriodOverOneMonth(startDate, endDate);
        this.validateStartDateBeforeToday(startDate);

        if(!responseBuilder.hasErrors()) {
            List<String> availabilities = siteAvailabilityRepository.getAvailabilitiesByDateAndSite(siteId, startDate, endDate)
                    .stream()
                    .map(SiteAvailability::getDayDate)
                    .map(LocalDate::toString)
                    .collect(Collectors.toList());

            responseBuilder.results(availabilities);
        }
        return responseBuilder.build();
    }

    private LocalDate getStartDate(String strStartDate) {
        if (strStartDate == null) {
            return LocalDate.now();
        } else if(!ValidatorHelper.isValidLocalDate(strStartDate)){
            responseBuilder.addError("Start date is not in a valid format yyyy-MM-dd.");
            return null;
        }
        return LocalDate.parse(strStartDate);
    }

    private LocalDate getEndDate(String strEndDate) {
        if (strEndDate == null) {
            return LocalDate.now().plusMonths(1);
        } else if(!ValidatorHelper.isValidLocalDate(strEndDate)){
            responseBuilder.addError("End date is not in a valid format yyyy-MM-dd.");
            return null;
        }
        return LocalDate.parse(strEndDate);
    }

    private void validateStartDateGreaterThanEndDate(LocalDate startDate, LocalDate endDate) {
        if (ValidatorHelper.isStartDateGreaterThanEndDate(startDate, endDate)) {
            responseBuilder.addError("Start date cannot be greater than the end date.");
        }
    }

    private void validatePeriodOverOneMonth(LocalDate startDate, LocalDate endDate) {
        if (ValidatorHelper.isPeriodOverOneMonth(startDate, endDate)) {
            responseBuilder.addError("The period between start date and end date exceed over a month.");
        }
    }

    private void validateStartDateBeforeToday(LocalDate startDate) {
        if (ValidatorHelper.isDateBeforeToday(startDate)) {
            responseBuilder.addError("Start date cannot be smaller than the current date.");
        }
    }
}
