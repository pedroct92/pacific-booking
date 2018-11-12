package com.challenge.pedrotorres.pacificbooking.proxies.campsite;

import com.challenge.pedrotorres.pacificbooking.api.requests.campsite.AvailabilityRequest;
import com.challenge.pedrotorres.pacificbooking.commons.Response;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
public interface SiteAvailabilityServiceProxy {

    String ADDRESS = SiteAvailabilityServiceProxy.class.getName();

    void getAvailabilitiesDatesByDateAndSite(AvailabilityRequest request, Handler<AsyncResult<Response>> resultHandler);

}
