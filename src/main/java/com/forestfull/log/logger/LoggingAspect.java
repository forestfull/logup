package com.forestfull.log.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {

    @Pointcut("@annotation(com.forestfull.log.config.Observable)")
    public void loggableMethods() {}

    @Before("loggableMethods()")
    public void logMethodCall(JoinPoint joinPoint) {
        System.out.println("Method " + joinPoint.getSignature().getName() + " is called with args: ");
        for (Object arg : joinPoint.getArgs()) {
            System.out.println("Parameter: " + arg);


        }
    }
}
