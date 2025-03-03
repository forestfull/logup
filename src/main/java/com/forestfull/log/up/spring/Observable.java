package com.forestfull.log.up.spring;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.formatter.LogFormatter;

import java.lang.annotation.*;

/**
 * <p>AOP functionality is supported only in Spring projects.
 * <p>If declared without enabling "arguments" and "returnValue", only method call detection is logged.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Observable {

    /**
     * Default placeholder pattern: {datetime} {level} - {msg}{new-line}
     * <p>- {datetime}: Date and time when the log is recorded
     * <p>- {level}: Log level
     * <p>- {msg}: The actual log message
     * <p>- {new-line}: New line character
     *
     * @return {@link String}
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     * @see LogFormatter.MessagePattern
     */
    String placeholder() default LogFormatter.MessagePattern.DATETIME + ' ' + LogFormatter.MessagePattern.LEVEL + " - " + LogFormatter.MessagePattern.MESSAGE + LogFormatter.MessagePattern.NEW_LINE;

    /**
     * It is the threshold log level for output configuration.
     *
     * @return {@link Level}
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    Level level() default Level.ALL;

    /**
     * The parameter values are logged every time the method is called.
     *
     * @return {@link Boolean}
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    boolean arguments() default false;

    /**
     * The returned value of the called method is logged.
     *
     * @return {@link Boolean}
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    boolean returnValue() default false; //
}