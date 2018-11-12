package com.challenge.pedrotorres.pacificbooking.repositories.booking;

import com.challenge.pedrotorres.pacificbooking.domain.booking.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {

}
