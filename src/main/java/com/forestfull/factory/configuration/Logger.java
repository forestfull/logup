package com.forestfull.factory.configuration;

import com.forestfull.factory.Level;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logger {
    Level level();
}