package com.challenge.pedrotorres.pacificbooking.verticles.workers;

import com.challenge.pedrotorres.pacificbooking.proxies.booking.BookingServiceProxy;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BookingWorker extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(BookingWorker.class);

    private BookingServiceProxy bookingServiceProxy;

    @Autowired
    public BookingWorker(BookingServiceProxy bookingServiceProxy) {
        this.bookingServiceProxy = bookingServiceProxy;
    }

    @Override
    public void start(Future<Void> startFuture) {
        new ServiceBinder(vertx)
                .setAddress(BookingServiceProxy.ADDRESS)
                .register(BookingServiceProxy.class, bookingServiceProxy)
                .completionHandler(ar -> {
                    if (ar.succeeded()) {
                        LOG.info("Worker started - " + BookingServiceProxy.ADDRESS);
                        startFuture.complete();
                    } else {
                        startFuture.fail(ar.cause());
                    }
                });
    }
}
