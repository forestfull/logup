package com.forestfull.factory.configuration;

import java.lang.annotation.*;
import java.util.logging.Level;


@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {
    Level level = null;
}