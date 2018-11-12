package com.challenge.pedrotorres.pacificbooking.proxies.booking;

import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.commons.Response;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
public interface BookingServiceProxy {

    String ADDRESS = BookingServiceProxy.class.getName();

    void addBooking(NewBookingRequest request, Handler<AsyncResult<Response>> resultHandler);
}
