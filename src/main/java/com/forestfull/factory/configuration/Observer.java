package com.forestfull.factory.configuration;

import java.lang.annotation.*;


@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observer {

    String pattern() default "";

}