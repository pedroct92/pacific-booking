package com.challenge.pedrotorres.pacificbooking.proxies.booking;

import com.challenge.pedrotorres.pacificbooking.api.requests.booking.CancelBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.ChangeBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.responses.Response;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
public interface BookingServiceProxy {

    String ADDRESS = BookingServiceProxy.class.getName();

    void getBooking(String bookingCode, Handler<AsyncResult<Response>> resultHandler);

    void addBooking(NewBookingRequest request, Handler<AsyncResult<Response>> resultHandler);

    void cancelBooking(CancelBookingRequest request, Handler<AsyncResult<Response>> resultHandler);

    void changeBooking(ChangeBookingRequest request, Handler<AsyncResult<Response>> resultHandler);

}
