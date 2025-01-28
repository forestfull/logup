package com.forestfull.log.logger;

import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ObservableAspect {

    @Setter
    private static String packageName;

    @Pointcut("execution(* *(..))")
    private void allMethods() {}

    @Before("allMethods()")
    public void logMethodCall(JoinPoint joinPoint) {
        System.out.println("Method " + joinPoint.getSignature().getName() + " is called with args: ");
        for (Object arg : joinPoint.getArgs()) {
            System.out.println("Parameter: " + arg);
        }
    }


    protected static String getCallerClassPackageName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        try {
            return Class.forName(stackTraceElements[4].getClassName()).getPackage().getName();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


}