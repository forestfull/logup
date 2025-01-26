package com.forestfull.devops.logger;

import lombok.Builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Builder
class LogAnnotationScanner {

    private Class<? extends Annotation> annotation;

    Class<?>[] getAnnotatedClasses() {
        return new Class[] {};
    }
}