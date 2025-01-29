package com.forestfull.log.logger;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {

    @Before("@annotation(com.forestfull.log.config.Observable)")
    public void logMethodCall(JoinPoint joinPoint) {
        System.out.println("Method " + joinPoint.getSignature().getName() + " is called with args: ");
        for (Object arg : joinPoint.getArgs()) {
            System.out.println("Parameter: " + arg);

        }
    }

    protected Class<?> getCallerClass() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        try {
            return Class.forName(stackTraceElements[5].getClassName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}