package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import com.forestfull.logger.spring.Observable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ObservableAspect {

    @Before("@annotation(com.forestfull.logger.spring.Observable)")
    void beforeMethod(JoinPoint joinPoint) {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method == null) return;

        Observable observable = method.getAnnotation(Observable.class);
        if (observable == null) return;

        final Level level = observable.level();
        final Object[] args = new Object[joinPoint.getArgs().length + 2];
        args[0] = joinPoint.getSignature().getName();
        if (joinPoint.getArgs().length > 1) {
            args[1] = " -> (";
            final Object[] joinPointArgs = joinPoint.getArgs();
            for (int i = 0; i < joinPointArgs.length; i++)
                args[i] = joinPointArgs[i] + (i + 1 < joinPointArgs.length ? ", " : "");

            args[joinPoint.getArgs().length] = ")";
        } else {
            args[1] = "(null)";
        }

        logOfLevel(args, joinPoint.getTarget(), method, level);
    }


    @AfterReturning(pointcut = "@annotation(com.forestfull.logger.spring.Observable)", returning = "returnValue")
    void afterMethod(JoinPoint joinPoint, Object returnValue) {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method == null) return;

        Observable observable = method.getAnnotation(Observable.class);
        if (observable == null) return;

        final Level level = observable.level();
        final Object[] args = new Object[joinPoint.getArgs().length + 1];
        args[0] = joinPoint.getSignature().getName() + " <- \"" + returnValue + "\"";

        logOfLevel(args, joinPoint.getTarget(), method,  level);

    }

    private static void logOfLevel(Object[] args, Object clazz, Method method, Level level) {
        final String placeholder = LogFormatter.MessagePattern.DATETIME
                + ' ' + LogFormatter.MessagePattern.LEVEL
                + ' ' + clazz.getClass().getName() + '.' + method.getName()
                + " - " + LogFormatter.MessagePattern.MESSAGE + Log.newLine;
        switch (level) {
            case ALL:
            case INFO:
                Log.write(Level.INFO, 0, placeholder, args);
                break;
            case WARN:
                Log.write(Level.WARN, 0, placeholder, args);
                break;
            case ERROR:
                Log.write(Level.ERROR, 0, placeholder, args);
                break;
        }
    }
}
