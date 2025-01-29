package com.forestfull.log.config;


import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {
    Level level() default Level.ALL;
}