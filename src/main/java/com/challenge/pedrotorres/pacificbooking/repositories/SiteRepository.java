package com.challenge.pedrotorres.pacificbooking.repositories;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import org.springframework.data.repository.CrudRepository;

public interface SiteRepository extends CrudRepository<Site, Long> {
}
