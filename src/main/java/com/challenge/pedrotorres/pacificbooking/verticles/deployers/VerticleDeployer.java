package com.challenge.pedrotorres.pacificbooking.verticles.deployers;

import com.challenge.pedrotorres.pacificbooking.App;
import com.challenge.pedrotorres.pacificbooking.api.BookingApi;
import com.challenge.pedrotorres.pacificbooking.verticles.factory.SpringVerticleFactory;
import com.challenge.pedrotorres.pacificbooking.verticles.workers.BookingWorkers;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class VerticleDeployer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private SpringVerticleFactory verticleFactory;

    @Value("${vertx.worker.pool.size}")
    private Integer workerPoolSize;

    @Value("${vertx.springWorker.instances}")
    private Integer springWorkerInstances;

    @Autowired
    public VerticleDeployer(SpringVerticleFactory verticleFactory) {
        this.verticleFactory = verticleFactory;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(workerPoolSize));

        vertx.registerVerticleFactory(verticleFactory);

        CountDownLatch deployLatch = new CountDownLatch(2);
        AtomicBoolean failed = new AtomicBoolean(false);

        String restApiVerticleName = verticleFactory.prefix() + ":" + BookingApi.class.getName();
        vertx.deployVerticle(restApiVerticleName, ar -> {
            if (ar.failed()) {
                LOG.error("Failed to deploy verticle", ar.cause());
                failed.compareAndSet(false, true);
            }
            deployLatch.countDown();
        });

        DeploymentOptions workerDeploymentOptions = new DeploymentOptions()
                .setWorker(true)
                .setInstances(springWorkerInstances);

        String workerVerticleName = verticleFactory.prefix() + ":" + BookingWorkers.class.getName();
        vertx.deployVerticle(workerVerticleName, workerDeploymentOptions, ar -> {
            if (ar.failed()) {
                LOG.error("Failed to deploy verticle", ar.cause());
                failed.compareAndSet(false, true);
            }
            deployLatch.countDown();
        });

        try {
            if (!deployLatch.await(5, SECONDS)) {
                throw new RuntimeException("Timeout waiting for verticle deployments");
            } else if (failed.get()) {
                throw new RuntimeException("Failure while deploying verticles");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
