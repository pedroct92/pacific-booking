package com.challenge.pedrotorres.pacificbooking.api;

import com.challenge.pedrotorres.pacificbooking.api.handlers.booking.BookingHandler;
import com.challenge.pedrotorres.pacificbooking.api.handlers.campsite.SiteAvailabilityHandler;
import com.challenge.pedrotorres.pacificbooking.proxies.booking.BookingServiceProxy;
import com.challenge.pedrotorres.pacificbooking.proxies.campsite.SiteAvailabilityServiceProxy;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookingApi extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(BookingApi.class);

    @Value("${app.conf.http.port}")
    private Integer httpPort;

    @Value("${app.conf.api.base.url}")
    private String baseApiUrl;

    private Router router;

    @Override
    public void start(Future<Void> startFuture) {
        this.router = this.createRouterWithHandlers();
        this.createHttpServer(startFuture);

        Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private Router createRouterWithHandlers() {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        BookingHandler bookingHandler = new BookingHandler(this.getBookingServiceProxy());
        SiteAvailabilityHandler siteAvailabilityHandler = new SiteAvailabilityHandler(this.getSiteAvailabilityServiceProxy());

        router.get(baseApiUrl + "/campsite/:site_id/availabilities").handler(siteAvailabilityHandler::getAvailabilitiesForCampsite);

        router.post(baseApiUrl + "/campsite/:site_id/booking").handler(bookingHandler::newBooking);
        router.get(baseApiUrl + "/campsite/booking/:booking_code").handler(bookingHandler::getBooking);
        router.put(baseApiUrl + "/campsite/booking/:booking_code").handler(bookingHandler::changeBooking);
        router.put(baseApiUrl + "/campsite/booking/:booking_code/cancel").handler(bookingHandler::cancelBooking);

        return router;
    }

    private void createHttpServer(Future<Void> startFuture) {
        vertx.createHttpServer().requestHandler(router::accept).listen(httpPort, listen -> {
            if (listen.succeeded()) {
                LOG.info("BookingApi started on the http port {}", httpPort);
                startFuture.complete();
            } else {
                LOG.error("BookingApi fail to start on the http port {}", httpPort);
                startFuture.fail(listen.cause());
            }
        });
    }

    private BookingServiceProxy getBookingServiceProxy() {
        return new ServiceProxyBuilder(vertx)
                    .setAddress(BookingServiceProxy.ADDRESS)
                    .build(BookingServiceProxy.class);
    }

    private SiteAvailabilityServiceProxy getSiteAvailabilityServiceProxy() {
        return new ServiceProxyBuilder(vertx)
                    .setAddress(SiteAvailabilityServiceProxy.ADDRESS)
                    .build(SiteAvailabilityServiceProxy.class);
    }
}
