package com.forestfull.log.logger.annotation;


import com.forestfull.log.logger.Level;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {
    Level level() default Level.ALL;
}
