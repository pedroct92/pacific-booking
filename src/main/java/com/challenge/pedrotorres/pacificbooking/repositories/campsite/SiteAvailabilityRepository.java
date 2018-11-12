package com.challenge.pedrotorres.pacificbooking.repositories.campsite;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.SiteAvailability;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SiteAvailabilityRepository extends CrudRepository<SiteAvailability, Long> {

    @Query("select a from SiteAvailability as a join Site as s on a.site.id = s.id " +
            " where a.remainingPlaces > 0 and a.dayDate >= :startDate and a.dayDate < :endDate and a.site.id = :siteId " +
            " and a.dayDate > CURRENT_DATE")
    List<SiteAvailability> getAvailabilitiesWithPlacesByDateAndSite(
            @Param("siteId") Long siteId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("select a from SiteAvailability as a join Site as s on a.site.id = s.id " +
            " where a.dayDate >= :startDate and a.dayDate < :endDate and a.site.id = :siteId " +
            " and a.dayDate > CURRENT_DATE")
    List<SiteAvailability> getAvailabilitiesByDateAndSite(
            @Param("siteId") Long siteId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
