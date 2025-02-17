package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import com.forestfull.logger.spring.Observable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ObservableAspect {

    @Around("@annotation(com.forestfull.logger.spring.Observable)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String targetFileName = null;
        int targetLine = -1;
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method == null) return null;

        final Observable observable = method.getAnnotation(Observable.class);
        if (observable == null) return null;


//        targetFileName = joinPoint.getSourceLocation().getFileName();
//        targetLine = joinPoint.getSourceLocation().getLine(); TODO: 프레임워크에 지원안한다고 리턴 값에 익셉션 박아놓은게 웃기지않나
/*

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            System.out.println(element.getClassName() + "." + element.getMethodName());
            if (element.getClassName().equals(AopProxyUtils.ultimateTargetClass(joinPoint.getThis()).getName()) && element.getMethodName().equals(joinPoint.getSignature().getName())) {
                targetFileName = element.getFileName();
                targetLine = element.getLineNumber();
                break;
            }
        }
*/

        final Level level = observable.level();

        StringBuilder argumentsBuilder;

        if (observable.arguments()) {
            argumentsBuilder = new StringBuilder();

            if (targetFileName != null && targetLine >= -1)
                argumentsBuilder.append('(').append(targetFileName).append(':').append(targetLine).append(')');

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

            logOfLevel(joinPoint.getTarget(), joinPoint, level, argumentsBuilder);
        } else if (!observable.returnValue()) {
            argumentsBuilder = new StringBuilder();

            if (targetFileName != null && targetLine >= -1)
                argumentsBuilder.append('(').append(targetFileName).append(':').append(targetLine).append(')');

            argumentsBuilder.append("@Observable detected calling: ").append(method.getName());

            logOfLevel(joinPoint.getTarget(), joinPoint, level, argumentsBuilder);
        }

        //TIP 메서드 실행
        Object returnValue = joinPoint.proceed();

        if (observable.returnValue()) {
            argumentsBuilder = new StringBuilder();

            if (targetFileName != null && targetLine >= -1)
                argumentsBuilder.append('(').append(targetFileName).append(':').append(targetLine).append(')');

            argumentsBuilder.append("@Observable detected return: ").append(method.getName());

            if (method.getReturnType() != Void.TYPE)
                argumentsBuilder.append(" <- (").append("(").append(returnValue.getClass().getName()).append(")\"").append(returnValue).append("\")");

            logOfLevel(joinPoint.getTarget(), joinPoint, level, argumentsBuilder);
        }

        return returnValue;
    }

    private static void logOfLevel(Object clazz, JoinPoint joinPoint, Level level, Object... args) {
        final String placeholder = LogFormatter.MessagePattern.DATETIME
                + ' ' + LogFormatter.MessagePattern.LEVEL
                + " - " + LogFormatter.MessagePattern.MESSAGE + Log.newLine;
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
