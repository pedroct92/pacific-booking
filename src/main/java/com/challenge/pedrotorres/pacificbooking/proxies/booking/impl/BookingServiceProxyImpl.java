package com.challenge.pedrotorres.pacificbooking.proxies.booking.impl;

import com.challenge.pedrotorres.pacificbooking.api.requests.booking.CancelBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.ChangeBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.responses.Response;
import com.challenge.pedrotorres.pacificbooking.proxies.booking.BookingServiceProxy;
import com.challenge.pedrotorres.pacificbooking.services.booking.BookingService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingServiceProxyImpl implements BookingServiceProxy {

    private BookingService bookingService;

    @Autowired
    public BookingServiceProxyImpl(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void getBooking(String bookingCode, Handler<AsyncResult<Response>> resultHandler) {
        Future.succeededFuture(this.bookingService.getBooking(bookingCode)).setHandler(resultHandler);
    }

    @Override
    public void addBooking(NewBookingRequest request, Handler<AsyncResult<Response>> resultHandler) {
        Future.succeededFuture(this.bookingService.addBooking(request)).setHandler(resultHandler);
    }

    @Override
    public void cancelBooking(CancelBookingRequest request, Handler<AsyncResult<Response>> resultHandler) {
        Future.succeededFuture(this.bookingService.cancelBooking(request)).setHandler(resultHandler);
    }

    @Override
    public void changeBooking(ChangeBookingRequest request, Handler<AsyncResult<Response>> resultHandler) {
        Future.succeededFuture(this.bookingService.changeBooking(request)).setHandler(resultHandler);
    }
}
