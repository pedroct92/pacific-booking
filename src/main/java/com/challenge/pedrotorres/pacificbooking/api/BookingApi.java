package com.challenge.pedrotorres.pacificbooking.api;

import com.challenge.pedrotorres.pacificbooking.api.requests.booking.NewBookingRequest;
import com.challenge.pedrotorres.pacificbooking.api.requests.campsite.AvailabilityRequest;
import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import com.challenge.pedrotorres.pacificbooking.proxies.booking.BookingServiceProxy;
import com.challenge.pedrotorres.pacificbooking.proxies.campsite.SiteAvailabilityServiceProxy;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

@Component
public class BookingApi extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(BookingApi.class);

    @Value("${app.conf.http.port}")
    private Integer httpPort;

    @Value("${app.conf.api.base.url}")
    private String baseApiUrl;

    private BookingServiceProxy bookingServiceProxy;

    private SiteAvailabilityServiceProxy siteAvailabilityServiceProxy;

    @Override
    public void start(Future<Void> startFuture) {
        bookingServiceProxy = new ServiceProxyBuilder(vertx)
                                    .setAddress(BookingServiceProxy.ADDRESS)
                                    .build(BookingServiceProxy.class);

        Router router = this.createRouterWithHandlers();
        this.createProxies();
        this.createHttpServer(startFuture, router);

        Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private Router createRouterWithHandlers() {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.post(baseApiUrl + "/booking").handler(this::newBooking);
        router.get(baseApiUrl + "/availabilities/:site_id").handler(this::getAvailabilitiesForCampsite);

        router.route().handler(StaticHandler.create());

        return router;
    }

    private void createHttpServer(Future<Void> startFuture, Router router) {
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

    private void createProxies() {
        bookingServiceProxy = new ServiceProxyBuilder(vertx)
                .setAddress(BookingServiceProxy.ADDRESS)
                .build(BookingServiceProxy.class);


        siteAvailabilityServiceProxy = new ServiceProxyBuilder(vertx)
                .setAddress(SiteAvailabilityServiceProxy.ADDRESS)
                .build(SiteAvailabilityServiceProxy.class);
    }

    private void getAvailabilitiesForCampsite(RoutingContext routingContext) {
        Site site = new Site();
        site.setId(Long.parseLong(routingContext.pathParam("site_id")));

        AvailabilityRequest request = new AvailabilityRequest();

        request.setSite(site);
        request.setStartDate(routingContext.queryParams().get("startDate"));
        request.setEndDate(routingContext.queryParams().get("endDate"));

        siteAvailabilityServiceProxy.getAvailabilitiesDatesByDateAndSite(request, ar -> {
            if (ar.succeeded()) {
                routingContext.response().setStatusCode(HTTP_OK).end(Json.encode(ar.result()));
            } else {
                routingContext.fail(ar.cause());
            }
        });
    }

    private void newBooking(RoutingContext routingContext) {
        NewBookingRequest request = new NewBookingRequest(routingContext.getBodyAsJson());

        bookingServiceProxy.addBooking(request, ar -> {
            if (ar.succeeded()) {
                routingContext.response().setStatusCode(HTTP_CREATED).end(Json.encode(ar.result()));
            } else {
                routingContext.fail(ar.cause());
            }
        });
    }
}
