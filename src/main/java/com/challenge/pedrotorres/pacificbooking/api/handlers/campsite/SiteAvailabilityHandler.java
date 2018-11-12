package com.challenge.pedrotorres.pacificbooking.api.handlers.campsite;

import com.challenge.pedrotorres.pacificbooking.api.requests.campsite.AvailabilityRequest;
import com.challenge.pedrotorres.pacificbooking.proxies.campsite.SiteAvailabilityServiceProxy;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_OK;

public class SiteAvailabilityHandler {

    private SiteAvailabilityServiceProxy siteAvailabilityServiceProxy;

    public SiteAvailabilityHandler(SiteAvailabilityServiceProxy siteAvailabilityServiceProxy) {
        this.siteAvailabilityServiceProxy = siteAvailabilityServiceProxy;
    }

    public void getAvailabilitiesForCampsite(RoutingContext routingContext) {
        AvailabilityRequest request = new AvailabilityRequest();

        request.setSiteId(Long.parseLong(routingContext.pathParam("site_id")));
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
}
