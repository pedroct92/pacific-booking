package com.challenge.pedrotorres.pacificbooking.services.booking;

import com.challenge.pedrotorres.pacificbooking.api.requests.booking.CancelBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.ChangeBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.responses.Response;

public interface BookingService {

    Response getBooking(String bookingCode);

    Response addBooking(NewBookingRequest request);

    Response changeBooking(ChangeBookingRequest request);

    Response cancelBooking(CancelBookingRequest request);
}
