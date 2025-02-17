package com.forestfull.logger.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ObservableAspect {

    @Before("@annotation(com.forestfull.logger.spring.Observable)")
    void beforeMethod(JoinPoint joinPoint) {
        System.out.println("Before method execution");
    }

    @After("@annotation(com.forestfull.logger.spring.Observable)")
    void afterMethod(JoinPoint joinPoint) {
        System.out.println("After method execution");
    }
}
