package com.forestfull.logger.util;

import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
@Builder
public class FileRecorder {
    @Builder.Default
    private String placeholder = getDefaultPlaceHolder();
    @Builder.Default
    private SimpleDateFormat dateFormat = getDefaultDateFormat();
    @Builder.Default
    private String logFileDirectory = "logs/";

    void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    void setLogFileDirectory(String logFileDirectory) {
        this.logFileDirectory = logFileDirectory;
    }

    static String getDefaultPlaceHolder() {
        return FileRecorder.FilePattern.PROJECT_NAME + "{date}.log";
    }

    static SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static class FilePattern {
        public static final String[] filePath = System.getProperty("user.dir").split("[/|\\\\]");
        public static final String PROJECT_NAME = filePath[filePath.length - 1];
        public static final String DATE = "{date}";
        public static final String DEFAULT = PROJECT_NAME + "{date}" + ".log";
    }
}
