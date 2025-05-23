package com.forestfull.log.up.formatter;

import com.forestfull.log.up.util.Log;
import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * The FileRecorder class provides settings and methods related to file logging.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Data
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
    private SimpleDateFormat dateFormat = new SimpleDateFormat(getDefaultDateFormat());

    /**
     * The directory where the log file will be stored.
     */
    @Builder.Default
    private String directory = "logs/";

    /**
     * Sets the date format for the log file.
     *
     * @param dateFormat the date format for the log file
     */
    public void setDateFormat(String dateFormat) {
        try {
            this.dateFormat = new SimpleDateFormat(dateFormat);
        } catch (NullPointerException | IllegalArgumentException e) {
            Log.LogFactory.builder().message("FileRecorder's DateFormat changed to default pattern because the given date format pattern is invalid" + System.lineSeparator()).build().run();
            this.dateFormat = new SimpleDateFormat(getDefaultDateFormat());
        }
    }

    /**
     * Sets the date format for the log file.
     *
     * @param dateFormat the date format for the log file
     */
    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Returns the default placeholder.
     *
     * @return the default placeholder string
     */
    public static String getDefaultPlaceHolder() {
        return FileRecorder.FilePattern.DEFAULT;
    }

    /**
     * Returns the default date format.
     *
     * @return the default date format
     */
    public static String getDefaultDateFormat() {
        return "yyyy-MM-dd";
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
