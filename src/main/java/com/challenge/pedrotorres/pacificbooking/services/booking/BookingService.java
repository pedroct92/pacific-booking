package com.challenge.pedrotorres.pacificbooking.services.booking;

import com.challenge.pedrotorres.pacificbooking.api.requests.booking.CancelBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.ChangeBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.commons.Response;

public interface BookingService {

    Response addBooking(NewBookingRequest request);

    Response changeBooking(ChangeBookingRequest request);

    Response cancelBooking(CancelBookingRequest request);
}
