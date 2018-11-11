package com.challenge.pedrotorres.pacificbooking.repositories;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.SiteAvailability;

import java.time.LocalDate;
import java.util.List;

public interface SiteAvailabilityRepository {

    List<SiteAvailability> getAvailabilities(Long siteId, LocalDate startDate, LocalDate endDate);

    void update(SiteAvailability siteAvailability);
}
