package com.challenge.pedrotorres.pacificbooking.services.booking;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;

@CacheConfig(cacheNames = "test")
public interface BookingService {

    @Cacheable
    List<Site> getAllSites();

    Site add(Site site);
}
