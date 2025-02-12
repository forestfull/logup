package com.forestfull.logger;


import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {
    Level level() default Level.ALL;
}
