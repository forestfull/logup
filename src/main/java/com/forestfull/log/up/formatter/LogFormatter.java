package com.forestfull.log.up.formatter;

import com.forestfull.log.up.util.Log;
import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * The LogFormatter class provides settings and methods for formatting log messages.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Data
@Builder
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
        try {
            this.dateTimeFormat = new SimpleDateFormat(dateTimeFormat);
        } catch (NullPointerException | IllegalArgumentException e) {
            Log.LogFactory.builder().message("LogFormatter's DateTimeFormat changed to default pattern because the given date format pattern is invalid" + System.lineSeparator()).build().run();
            this.dateTimeFormat = new SimpleDateFormat(getDefaultDateTimeFormat());
        }
    }

    public void setDateTimeFormat(SimpleDateFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
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
         * This feature measures CPU usage at intervals as short as 1 second and visualizes the results.
         */
        public static final String CPU_TICK = "{cpu}";

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
         * Default log format combining date-time, level, thread, message, and a new line.
         */
        public static final String DEFAULT = DATETIME + ' ' + CPU_TICK + ' ' + LEVEL + ' ' + THREAD + " - " + MESSAGE;
    }
}