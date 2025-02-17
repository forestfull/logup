package com.forestfull.logger.lombok;

import com.forestfull.logger.Level;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
@interface Observable {
    Level level() default Level.ALL;
}
