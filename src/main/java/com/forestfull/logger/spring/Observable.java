package com.forestfull.logger.spring;

import com.forestfull.logger.Level;

import java.lang.annotation.*;

/**
 * <p>AOP functionality is supported only in Spring projects.
 * <p>If declared without enabling "arguments" and "returnValue", only method call detection is logged.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {

    /**
     * It is the threshold log level for output configuration.
     *
     * @return {@link Level}
     */
    Level level() default Level.ALL;

    /**
     * The parameter values are logged every time the method is called.
     *
     * @return {@link Boolean}
     */
    boolean arguments() default false;

    /**
     * The returned value of the called method is logged.
     *
     * @return {@link Boolean}
     */
    boolean returnValue() default false; //
}