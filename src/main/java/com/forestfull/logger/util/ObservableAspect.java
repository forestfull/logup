package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import com.forestfull.logger.spring.Observable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class ObservableAspect {

    @Around("@annotation(com.forestfull.logger.spring.Observable)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method == null) return null;

        final Observable observable = method.getAnnotation(Observable.class);
        if (observable == null) return null;

        final Level level = observable.level();
        String placeholder = observable.placeholder();

        StringBuilder argumentsBuilder;

        if (observable.arguments()) {
            argumentsBuilder = new StringBuilder();
            argumentsBuilder.append("@Observable detected calling: ").append(method.getName());

            if (method.getParameterTypes().length != 0) {
                argumentsBuilder.append(" -> (");
                final Object[] joinPointArgs = joinPoint.getArgs();

                for (int i = 0; i < joinPointArgs.length; i++) {
                    argumentsBuilder.append("(").append(joinPointArgs[i].getClass().getName()).append(")").append(joinPointArgs[i])
                            .append(i + 1 < joinPointArgs.length ? ", " : "");
                }
                argumentsBuilder.append(')');

            } else {
                argumentsBuilder.append("()");
            }

            logOfLevel(placeholder, level, argumentsBuilder);
        } else if (!observable.returnValue()) {
            argumentsBuilder = new StringBuilder();
            argumentsBuilder.append("@Observable detected calling: ").append(method.getName());

            logOfLevel(placeholder, level, argumentsBuilder);
        }

        //TIP 메서드 실행
        Object returnValue = joinPoint.proceed();

        if (observable.returnValue()) {
            argumentsBuilder = new StringBuilder();
            argumentsBuilder.append("@Observable detected return: ").append(method.getName());

            if (method.getReturnType() != Void.TYPE)
                argumentsBuilder.append(" <- (").append("(").append(returnValue.getClass().getName()).append(")\"").append(returnValue).append("\")");

            logOfLevel(placeholder, level, argumentsBuilder);
        }

        return returnValue;
    }

    private static void logOfLevel(String declaredPlaceholder, final Level level, final Object... args) {
        String placeholder = declaredPlaceholder;
        if (placeholder == null || placeholder.trim().isEmpty()) {
            placeholder = LogFormatter.MessagePattern.DATETIME
                    + ' ' + LogFormatter.MessagePattern.LEVEL
                    + " - " + LogFormatter.MessagePattern.MESSAGE + Log.newLine;
        }

        switch (level) {
            case ALL:
            case INFO:
                Log.write(Level.INFO, placeholder, args);
                break;
            case WARN:
                Log.write(Level.WARN, placeholder, args);
                break;
            case ERROR:
                Log.write(Level.ERROR, placeholder, args);
                break;
        }
    }
}