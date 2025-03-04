package com.forestfull.log.up.formatter;

import lombok.*;

import java.text.SimpleDateFormat;

/**
 * The LogFormatter class provides settings and methods for formatting log messages.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogFormatter {
    /**
     * The format string for the log messages.
     */
    @Builder.Default
    private String placeholder = MessagePattern.DEFAULT;

    /**
     * The date and time format for the log messages.
     */
    @Builder.Default
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(getDefaultDateTimeFormat());

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = new SimpleDateFormat(dateTimeFormat);
    }

    /**
     * Returns the default placeholder format string.
     *
     * @return the default placeholder format string
     */
    public static String getDefaultPlaceHolder() {
        return MessagePattern.DEFAULT;
    }

    /**
     * Returns the default date and time format.
     *
     * @return the default date and time format
     */
    public static String getDefaultDateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss.sss";
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
         * Placeholder for the stack trace.
         */
        public static final String STACKTRACE = "{stacktrace}";

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
        public static final String DEFAULT = DATETIME + ' ' + LEVEL + ' ' + THREAD + ' ' + STACKTRACE + " - " + MESSAGE + NEW_LINE;
    }
}