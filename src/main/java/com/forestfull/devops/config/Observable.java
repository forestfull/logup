package com.forestfull.devops.config;


import java.lang.annotation.*;
import java.util.logging.Level;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {
    Level level = Level.ALL;
}