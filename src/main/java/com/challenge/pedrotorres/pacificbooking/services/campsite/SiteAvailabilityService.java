package com.challenge.pedrotorres.pacificbooking.services.campsite;

import com.challenge.pedrotorres.pacificbooking.api.requests.campsite.AvailabilityRequest;
import com.challenge.pedrotorres.pacificbooking.api.responses.Response;
import org.springframework.cache.annotation.Cacheable;

public interface SiteAvailabilityService {

    @Cacheable(value = "availabilitiesDates", keyGenerator = "customKeyGenerator")
    Response getAvailabilitiesDatesByDateAndSite(AvailabilityRequest request);
}
