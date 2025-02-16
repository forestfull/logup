package com.forestfull.logger.util;

import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
@Builder
public class LogFormatter {
    private String placeholder;
    private SimpleDateFormat dateTimeFormat;

    protected void setDateTimeFormat(SimpleDateFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    protected void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    protected static String getDefaultPlaceHolder() {
        return MessagePattern.DEFAULT;
    }

    protected static SimpleDateFormat getDefaultDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static class MessagePattern {
        public static final String DATETIME = "{datetime}";
        public static final String THREAD = "{thread}";
        public static final String LEVEL = "{level}";
        public static final String MESSAGE = "{msg}";
        public static final String NEW_LINE = "{new-line}";
        public static final String DEFAULT = DATETIME + " " + THREAD + " " + LEVEL + " - " + MESSAGE + NEW_LINE;
    }
}
