package com.forestfull.log.up.util;

import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

/**
 * The FileRecorder class provides settings and methods related to file logging.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Getter
@Builder
public class FileRecorder {
    /**
     * The default format for the log file name.
     */
    @Builder.Default
    private String placeholder = getDefaultPlaceHolder();

    /**
     * The date format for the log file.
     */
    @Builder.Default
    private SimpleDateFormat dateFormat = getDefaultDateFormat();

    /**
     * The directory where the log file will be stored.
     */
    @Builder.Default
    private String logFileDirectory = "logs/";

    /**
     * Sets the format for the log file name.
     *
     * @param placeholder the format for the log file name
     */
    void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Sets the date format for the log file.
     *
     * @param dateFormat the date format for the log file
     */
    void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Sets the directory where the log file will be stored.
     *
     * @param logFileDirectory the directory where the log file will be stored
     */
    void setLogFileDirectory(String logFileDirectory) {
        this.logFileDirectory = logFileDirectory;
    }

    /**
     * Returns the default placeholder.
     *
     * @return the default placeholder string
     */
    static String getDefaultPlaceHolder() {
        return FileRecorder.FilePattern.PROJECT_NAME + "{date}.log";
    }

    /**
     * Returns the default date format.
     *
     * @return the default date format
     */
    static SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * Provides patterns and constants for file naming and path handling.
     *
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static class FilePattern {
        /**
         * Array of directory names in the user's current working directory path.
         * This is obtained by splitting the user directory path using file separator characters.
         */
        public static final String[] filePath = System.getProperty("user.dir").split("[/|\\\\]");

        /**
         * The name of the project, derived from the last element of the user's directory path.
         */
        public static final String PROJECT_NAME = filePath[filePath.length - 1];

        /**
         * Placeholder for the date when the log is created.
         *
         * @see SimpleDateFormat
         */
        public static final String DATE = "{date}";

        /**
         * Default log file naming pattern combining the project name and date placeholder.
         * The resulting format will be: "ProjectName{date}.log"
         */
        public static final String DEFAULT = PROJECT_NAME + "{date}" + ".log";
    }
}
