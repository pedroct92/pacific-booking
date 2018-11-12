package com.challenge.pedrotorres.pacificbooking.cache;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getName() + "_" + method.getName() + "_" + HashCodeBuilder.reflectionHashCode(params);
    }
}
