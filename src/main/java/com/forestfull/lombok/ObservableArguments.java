package com.forestfull.lombok;


import com.forestfull.logger.Level;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ObservableArguments {
    Level level() default Level.ALL;
}
