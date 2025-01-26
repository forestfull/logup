package com.forestfull.devops.logger;

import lombok.Builder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Builder
class ObservableLogHandler implements InvocationHandler {

    private Class<?>[] target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(target);

        return null;
    }
}
