package com.challenge.pedrotorres.pacificbooking.repositories.impl;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.SiteAvailability;
import com.challenge.pedrotorres.pacificbooking.repositories.SiteAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SiteAvailabilityRepositoryImpl implements SiteAvailabilityRepository {

    @Autowired
    private EntityManager entityManager;


    @Override
    public List<SiteAvailability> getAvailabilities(Long siteId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public void update(SiteAvailability siteAvailability) {

    }
}
