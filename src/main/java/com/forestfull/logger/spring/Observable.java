package com.forestfull.logger.spring;

import com.forestfull.logger.Level;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {
    Level level() default Level.ALL;
}
