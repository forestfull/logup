package com.forestfull.log.config;


import java.lang.annotation.*;
import java.util.logging.Level;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {
    Level level = Level.ALL;
}