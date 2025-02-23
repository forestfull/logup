package com.forestfull.log.up.util;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.spring.Observable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Aspect for handling methods annotated with {@link Observable}.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Aspect
public class ObservableAspect {

    /**
     * Intercepts method calls annotated with {@link Observable} and logs method calls and return values based on the level and placeholder.
     *
     * @param joinPoint the join point representing the method call
     * @return the result of the method call
     * @throws Throwable if the method call fails
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    @Around("@annotation(com.forestfull.log.up.spring.Observable)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method == null) return null;

        final Observable observable = method.getAnnotation(Observable.class);
        if (observable == null) return null;

        final Level level = observable.level();
        String placeholder = observable.placeholder();

        if (placeholder == null || placeholder.trim().isEmpty()) {
            placeholder = LogFormatter.MessagePattern.DATETIME
                    + ' ' + LogFormatter.MessagePattern.LEVEL
                    + " - " + LogFormatter.MessagePattern.MESSAGE + System.lineSeparator();
        }

        placeholder = placeholder.replaceAll(LogFormatter.MessagePattern.THREAD, "");

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

        // Execute the method
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

    /**
     * Logs the message at the specified level using the provided placeholder and arguments.
     *
     * @param placeholder the placeholder format for the log message
     * @param level       the log level
     * @param args        the arguments for the log message
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    private static void logOfLevel(String placeholder, final Level level, final Object... args) {
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
