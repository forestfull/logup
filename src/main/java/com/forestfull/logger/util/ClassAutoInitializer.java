package com.forestfull.logger.util;

import com.forestfull.logger.Observable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ClassAutoInitializer {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        boolean hasArgAnnotation = method.isAnnotationPresent(Observable.class);

                        if (hasArgAnnotation) {
                            System.out.println("LOG: Executing method with @ObservableArguments -> " + method.getName());
                            System.out.println("LOG: Actual Parameters -> " + Arrays.toString(args));
                        }

                    }
                }
        );
    }
}
