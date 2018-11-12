package com.challenge.pedrotorres.pacificbooking.verticles.workers;

import com.challenge.pedrotorres.pacificbooking.proxies.campsite.SiteAvailabilityServiceProxy;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AvailabilityWorker extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(AvailabilityWorker.class);

    private SiteAvailabilityServiceProxy siteAvailabilityServiceProxy;

    @Autowired
    public AvailabilityWorker(SiteAvailabilityServiceProxy siteAvailabilityServiceProxy) {
        this.siteAvailabilityServiceProxy = siteAvailabilityServiceProxy;
    }

    @Override
    public void start(Future<Void> startFuture) {
        new ServiceBinder(vertx)
                .setAddress(SiteAvailabilityServiceProxy.ADDRESS)
                .register(SiteAvailabilityServiceProxy.class, siteAvailabilityServiceProxy)
                .completionHandler(ar -> {
                    if (ar.succeeded()) {
                        LOG.info("Worker started - " + SiteAvailabilityServiceProxy.ADDRESS);
                        startFuture.complete();
                    } else {
                        startFuture.fail(ar.cause());
                    }
                });
    }
}
