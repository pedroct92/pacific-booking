package com.challenge.pedrotorres.pacificbooking.proxies.booking.impl;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import com.challenge.pedrotorres.pacificbooking.proxies.booking.BookingServiceProxy;
import com.challenge.pedrotorres.pacificbooking.services.booking.BookingService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingServiceProxyImpl implements BookingServiceProxy {

    private BookingService bookingService;

    @Autowired
    public BookingServiceProxyImpl(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void getAllSites(Handler<AsyncResult<List<Site>>> resultHandler) {
        Future.succeededFuture(this.bookingService.getAllSites()).setHandler(resultHandler);
    }

    @Override
    public void add(Site site, Handler<AsyncResult<Site>> resultHandler) {
        Future.succeededFuture(this.bookingService.add(site)).setHandler(resultHandler);
    }
}
