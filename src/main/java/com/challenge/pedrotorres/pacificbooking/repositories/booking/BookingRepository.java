package com.challenge.pedrotorres.pacificbooking.repositories.booking;

import com.challenge.pedrotorres.pacificbooking.domain.booking.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends CrudRepository<Booking, Long> {

    @Query("select b from Booking as b where b.code = :bookingCode")
    Booking getBookingByCode(
            @Param("bookingCode") String bookingCode
    );
}
