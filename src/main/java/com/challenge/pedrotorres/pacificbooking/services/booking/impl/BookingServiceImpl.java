package com.challenge.pedrotorres.pacificbooking.services.booking.impl;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import com.challenge.pedrotorres.pacificbooking.repositories.SiteRepository;
import com.challenge.pedrotorres.pacificbooking.services.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private SiteRepository siteRepository;

    @Autowired
    public BookingServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public List<Site> getAllSites() {
        return StreamSupport.stream(this.siteRepository.findAll().spliterator(), false)
                            .collect(toList());
    }

    @Override
    public Site add(Site site) {
        return this.siteRepository.save(site);
    }
}
