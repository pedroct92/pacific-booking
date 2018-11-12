package com.challenge.pedrotorres.pacificbooking.api.handlers.booking;

import com.challenge.pedrotorres.pacificbooking.api.requests.booking.CancelBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.ChangeBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.responses.Response;
import com.challenge.pedrotorres.pacificbooking.proxies.booking.BookingServiceProxy;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class BookingHandler {

    private BookingServiceProxy bookingServiceProxy;

    public BookingHandler(BookingServiceProxy bookingServiceProxy) {
        this.bookingServiceProxy = bookingServiceProxy;
    }

    public void getBooking(RoutingContext routingContext) {
        String bookingCode = routingContext.pathParam("booking_code");

        bookingServiceProxy.getBooking(bookingCode, ar -> {
            if (ar.succeeded()) {
                Response response = ar.result();
                routingContext.response().setStatusCode(response.getStatus()).end(Json.encode(response));
            } else {
                routingContext.fail(ar.cause());
            }
        });
    }


    public void newBooking(RoutingContext routingContext) {
        NewBookingRequest request = new NewBookingRequest(routingContext.getBodyAsJson());

        request.setCampsiteId(Long.parseLong(routingContext.pathParam("site_id")));

        bookingServiceProxy.addBooking(request, ar -> {
            if (ar.succeeded()) {
                Response response = ar.result();
                routingContext.response().setStatusCode(response.getStatus()).end(Json.encode(response));
            } else {
                routingContext.fail(ar.cause());
            }
        });
    }

    public void changeBooking(RoutingContext routingContext) {
        ChangeBookingRequest request = new ChangeBookingRequest(routingContext.getBodyAsJson());
        request.setBookingCode(routingContext.pathParam("booking_code"));

        bookingServiceProxy.changeBooking(request, ar -> {
            if (ar.succeeded()) {
                Response response = ar.result();
                routingContext.response().setStatusCode(response.getStatus()).end(Json.encode(response));
            } else {
                routingContext.fail(ar.cause());
            }
        });
    }

    public void cancelBooking(RoutingContext routingContext) {
        CancelBookingRequest request = new CancelBookingRequest();
        request.setBookingCode(routingContext.pathParam("booking_code"));

        bookingServiceProxy.cancelBooking(request, ar -> {
            if (ar.succeeded()) {
                Response response = ar.result();
                routingContext.response().setStatusCode(response.getStatus()).end(Json.encode(response));
            } else {
                routingContext.fail(ar.cause());
            }
        });
    }
}
