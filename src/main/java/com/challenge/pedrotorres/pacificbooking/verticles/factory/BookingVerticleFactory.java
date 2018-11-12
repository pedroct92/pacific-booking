package com.challenge.pedrotorres.pacificbooking.verticles.factory;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BookingVerticleFactory implements VerticleFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final String VERTICLE_PREFIX = "booking-app";

    @Override
    public boolean blockingCreate() {
        return true;
    }

    @Override
    public String prefix() {
        return VERTICLE_PREFIX;
    }

    @Override
    public Verticle createVerticle(String name, ClassLoader classLoader) throws Exception {
        String clazz = VerticleFactory.removePrefix(name);
        return (Verticle) applicationContext.getBean(Class.forName(clazz));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String getVerticleName(String className) {
        return this.prefix() + ":" + className;
    }
}
