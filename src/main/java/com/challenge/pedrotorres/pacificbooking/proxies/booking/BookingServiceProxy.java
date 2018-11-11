package com.challenge.pedrotorres.pacificbooking.proxies.booking;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

@ProxyGen
public interface BookingServiceProxy {

    String ADDRESS = BookingServiceProxy.class.getName();

    void getAllSites(Handler<AsyncResult<List<Site>>> resultHandler);

    void add(Site site, Handler<AsyncResult<Site>> resultHandler);
}
