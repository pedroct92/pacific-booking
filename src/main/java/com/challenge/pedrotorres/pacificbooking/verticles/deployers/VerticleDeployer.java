package com.challenge.pedrotorres.pacificbooking.verticles.deployers;

import com.challenge.pedrotorres.pacificbooking.App;
import com.challenge.pedrotorres.pacificbooking.api.BookingApi;
import com.challenge.pedrotorres.pacificbooking.verticles.factory.BookingVerticleFactory;
import com.challenge.pedrotorres.pacificbooking.verticles.workers.AvailabilityWorker;
import com.challenge.pedrotorres.pacificbooking.verticles.workers.BookingWorker;
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

    private static final Integer NUMBER_OF_VERTICLES = 3;

    private BookingVerticleFactory verticleFactory;

    @Value("${vertx.worker.pool.size}")
    private Integer workerPoolSize;

    @Value("${vertx.springWorker.instances}")
    private Integer springWorkerInstances;

    private Vertx vertx;

    private CountDownLatch deployLatch = new CountDownLatch(NUMBER_OF_VERTICLES);

    private AtomicBoolean failedDeployment = new AtomicBoolean(false);

    @Autowired
    public VerticleDeployer(BookingVerticleFactory verticleFactory) {
        this.verticleFactory = verticleFactory;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(workerPoolSize));
        vertx.registerVerticleFactory(verticleFactory);

        this.deployRestApi();
        this.deployBookingWorkers();
        this.deployAvailabilityWorkers();
        this.checkDeploymentStatus();
    }

    private void deployRestApi() {
        String restApiVerticleName = verticleFactory.getVerticleName(BookingApi.class.getName());

        vertx.deployVerticle(restApiVerticleName, ar -> {
            if (ar.failed()) {
                LOG.error("Failed to deploy {}: {}", restApiVerticleName, ar.cause());
                failedDeployment.compareAndSet(false, true);
            }
            deployLatch.countDown();
        });
    }

    private void deployBookingWorkers() {
        String workerVerticleName = verticleFactory.getVerticleName(BookingWorker.class.getName());

        DeploymentOptions deploymentOptions = new DeploymentOptions()
                .setWorker(true)
                .setInstances(springWorkerInstances);

        vertx.deployVerticle(workerVerticleName, deploymentOptions, ar -> {
            if (ar.failed()) {
                LOG.error("Failed to deploy {}: {}", workerVerticleName, ar.cause());
                failedDeployment.compareAndSet(false, true);
            }
            deployLatch.countDown();
        });
    }

    private void deployAvailabilityWorkers() {
        String workerVerticleName = verticleFactory.getVerticleName(AvailabilityWorker.class.getName());

        DeploymentOptions deploymentOptions = new DeploymentOptions()
                .setWorker(true)
                .setInstances(springWorkerInstances);

        vertx.deployVerticle(workerVerticleName, deploymentOptions, ar -> {
            if (ar.failed()) {
                LOG.error("Failed to deploy {}: {}", workerVerticleName, ar.cause());
                failedDeployment.compareAndSet(false, true);
            }
            deployLatch.countDown();
        });
    }

    private void checkDeploymentStatus() {
        try {
            if (!deployLatch.await(5, SECONDS)) {
                throw new RuntimeException("Timeout waiting for verticle deployments");
            } else if (failedDeployment.get()) {
                throw new RuntimeException("Failure while deploying verticles");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
