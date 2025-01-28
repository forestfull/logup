package com.forestfull.log.logger;

import lombok.Builder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

@Builder
class ObservableLogHandler implements InvocationHandler {

    private Set<Class<?>> target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(target);

        return null;
    }
}