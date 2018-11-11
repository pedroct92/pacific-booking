package com.challenge.pedrotorres.pacificbooking.api;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import com.challenge.pedrotorres.pacificbooking.proxies.booking.BookingServiceProxy;
import com.google.gson.Gson;
import io.vertx.core.Future;
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

@Component
public class BookingApi extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(BookingApi.class);

    private static final Gson gson = new Gson();

    @Value("${app.conf.http.port}")
    private Integer httpPort;

    private BookingServiceProxy bookingServiceProxy;

    @Override
    public void start(Future<Void> startFuture) {
        bookingServiceProxy = new ServiceProxyBuilder(vertx)
                                    .setAddress(BookingServiceProxy.ADDRESS)
                                    .build(BookingServiceProxy.class);

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.get("/booking").handler(this::getBookingCount);
        router.post("/booking").handler(this::newBooking);


        StaticHandler staticHandler = StaticHandler.create();
        router.route().handler(staticHandler);

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

    private void newBooking(RoutingContext routingContext) {
        Site site = new Site(routingContext.getBodyAsJson());

        bookingServiceProxy.add(site, ar -> {
            if (ar.succeeded()) {
                routingContext.response().setStatusCode(HTTP_CREATED).end(gson.toJson(ar.result()));
            } else {
                routingContext.fail(ar.cause());
            }
        });
    }

    private void getBookingCount(RoutingContext routingContext) {
        bookingServiceProxy.getAllSites(ar -> {
            if (ar.succeeded()) {
                routingContext.response().end(ar.result().size() + "");
            } else {
                routingContext.fail(ar.cause());
            }
        });
    }
}
