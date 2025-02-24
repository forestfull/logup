package com.forestfull.log.up.util;

import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

/**
 * The LogFormatter class provides settings and methods for formatting log messages.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Getter
@Builder
public class LogFormatter {
    /**
     * The format string for the log messages.
     */
    private String placeholder;

    /**
     * The date and time format for the log messages.
     */
    private SimpleDateFormat dateTimeFormat;

    /**
     * Sets the date and time format for the log messages.
     *
     * @param dateTimeFormat the date and time format
     */
    void setDateTimeFormat(SimpleDateFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    /**
     * Sets the format string for the log messages.
     *
     * @param placeholder the format string
     */
    void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Returns the default placeholder format string.
     *
     * @return the default placeholder format string
     */
    static String getDefaultPlaceHolder() {
        return MessagePattern.DEFAULT;
    }

    /**
     * Returns the default date and time format.
     *
     * @return the default date and time format
     */
    static SimpleDateFormat getDefaultDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Defines placeholders used in log formatting.
     *
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static class MessagePattern {
        /**
         * Placeholder for the date and time when the log is recorded.
         *
         * @see SimpleDateFormat
         */
        public static final String DATETIME = "{datetime}";

        /**
         * Placeholder for the thread that generated the log.
         */
        public static final String THREAD = "{thread}";

        /**
         * Placeholder for the log level.
         *
         * @see com.forestfull.log.up.Level
         */
        public static final String LEVEL = "{level}";

        /**
         * Placeholder for the actual log message.
         */
        public static final String MESSAGE = "{msg}";

        /**
         * Placeholder for a new line character.
         */
        public static final String NEW_LINE = "{new-line}";

        /**
         * Default log format combining date-time, level, thread, message, and a new line.
         */
        public static final String DEFAULT = DATETIME + ' ' + LEVEL + ' ' + THREAD + " - " + MESSAGE + NEW_LINE;
    }
}
