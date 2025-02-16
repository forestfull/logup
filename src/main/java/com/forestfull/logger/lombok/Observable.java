package com.forestfull.logger.lombok;


import com.forestfull.logger.Level;

import java.lang.annotation.*;

@Documented
@Target(ElementType.LOCAL_VARIABLE)
@Retention(RetentionPolicy.RUNTIME)
@interface Observable {
    Level level() default Level.ALL;
}
