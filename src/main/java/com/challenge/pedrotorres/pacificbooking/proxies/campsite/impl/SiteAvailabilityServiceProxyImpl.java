package com.challenge.pedrotorres.pacificbooking.proxies.campsite.impl;

import com.challenge.pedrotorres.pacificbooking.api.requests.campsite.AvailabilityRequest;
import com.challenge.pedrotorres.pacificbooking.api.responses.Response;
import com.challenge.pedrotorres.pacificbooking.proxies.campsite.SiteAvailabilityServiceProxy;
import com.challenge.pedrotorres.pacificbooking.services.campsite.SiteAvailabilityService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SiteAvailabilityServiceProxyImpl implements SiteAvailabilityServiceProxy {

    private SiteAvailabilityService siteAvailabilityService;


    @Autowired
    public SiteAvailabilityServiceProxyImpl(SiteAvailabilityService siteAvailabilityService) {
        this.siteAvailabilityService = siteAvailabilityService;
    }

    @Override
    public void getAvailabilitiesDatesByDateAndSite(AvailabilityRequest request, Handler<AsyncResult<Response>> resultHandler) {
        Future.succeededFuture(this.siteAvailabilityService.getAvailabilitiesDatesByDateAndSite(request)).setHandler(resultHandler);
    }
}
