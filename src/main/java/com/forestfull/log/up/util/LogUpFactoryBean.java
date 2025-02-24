package com.forestfull.log.up.util;

import com.forestfull.log.up.Level;

import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * LogUp's general Configuration (LogUp 전역 환경 설정)
 *
 * <ul>
 * <li><b>OFF</b>
 * <li><b>ERROR</b>
 * <li><b>WARN</b>
 * <li><b>INFO</b>
 * <li><b>ALL</b>
 * </ul>
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 * @see Log
 */

public class LogUpFactoryBean {
    static LogFormatter logFormatter;
    static FileRecorder fileRecorder;
    static Level level;

    static {
        configureProperties();

        if (LogUpFactoryBean.level == null)
            loggingInitializeManual();

        defaultInitialize();
    }

    private static void loggingInitializeManual() {
        Log.LogFactory.console(System.lineSeparator() + "=================================================================================================================================================================" + System.lineSeparator());
        Log.LogFactory.console("LogUp Setting Example");
        Log.LogFactory.console(System.lineSeparator() + "=================================================================================================================================================================" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.1 - application.properties" + System.lineSeparator());
        Log.LogFactory.console("logup.level=INFO" + System.lineSeparator());
        Log.LogFactory.console("logup.log-format.placeholder={datetime} {level} {thread} - {msg}{new-line}" + System.lineSeparator());
        Log.LogFactory.console("logup.log-format.date-time-format=yyyy-MM-dd HH:mm:ss" + System.lineSeparator());
        Log.LogFactory.console("logup.file-recode.directory=log/" + System.lineSeparator());
        Log.LogFactory.console("logup.file-recode.placeholder=" + FileRecorder.FilePattern.PROJECT_NAME + "-{date}.log" + System.lineSeparator());
        Log.LogFactory.console("logup.file-recode.date-format=yyyy-MM-dd" + System.lineSeparator() + System.lineSeparator());
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.2 - application.yml" + System.lineSeparator());
        Log.LogFactory.console("logup:" + System.lineSeparator());
        Log.LogFactory.console("  level: INFO # ALL, INFO, WARN, ERROR, OFF" + System.lineSeparator());
        Log.LogFactory.console("  log-format:" + System.lineSeparator());
        Log.LogFactory.console("    placeholder: \"{datetime} {level} {thread} - {msg}{new-line}\"" + System.lineSeparator());
        Log.LogFactory.console("    date-time-format: yyyy-MM-dd HH:mm:ss" + System.lineSeparator());
        Log.LogFactory.console("  file-recode:" + System.lineSeparator());
        Log.LogFactory.console("    directory: log/ # is default" + System.lineSeparator());
        Log.LogFactory.console("    placeholder: " + FileRecorder.FilePattern.PROJECT_NAME + "-{date}.log" + System.lineSeparator());
        Log.LogFactory.console("    date-format: yyyy-MM-dd" + System.lineSeparator() + System.lineSeparator());
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.3 - source code" + System.lineSeparator());
        Log.LogFactory.console("LogUpFactoryBean.builder()" + System.lineSeparator());
        Log.LogFactory.console("                    .level(Level.INFO)" + System.lineSeparator());
        Log.LogFactory.console("                    .logFormatter(LogFormatter.builder().build())" + System.lineSeparator());
        Log.LogFactory.console("                    .fileRecorder(FileRecorder.builder().logFileDirectory(\"logs/\").build())" + System.lineSeparator());
        Log.LogFactory.console("                    .build();" + System.lineSeparator() + System.lineSeparator());
        Log.LogFactory.console("=================================================================================================================================================================" + System.lineSeparator() + System.lineSeparator());
    }

    private static void defaultInitialize() {
        if (LogUpFactoryBean.level == null)
            LogUpFactoryBean.level = Level.ALL;

        if (LogUpFactoryBean.logFormatter == null)
            LogUpFactoryBean.logFormatter = LogFormatter.builder().build();

        if (LogUpFactoryBean.logFormatter.getPlaceholder() == null)
            LogUpFactoryBean.logFormatter.setPlaceholder(LogFormatter.getDefaultPlaceHolder());

        if (LogUpFactoryBean.logFormatter.getDateTimeFormat() == null)
            LogUpFactoryBean.logFormatter.setDateTimeFormat(LogFormatter.getDefaultDateTimeFormat());

        if (LogUpFactoryBean.fileRecorder == null) return;

        if (LogUpFactoryBean.fileRecorder.getPlaceholder() == null)
            LogUpFactoryBean.fileRecorder.setPlaceholder(FileRecorder.getDefaultPlaceHolder());

        if (LogUpFactoryBean.fileRecorder.getDateFormat() == null)
            LogUpFactoryBean.fileRecorder.setDateFormat(FileRecorder.getDefaultDateFormat());
    }

    private static void configureProperties() {
        final Properties properties = ConfigLoader.loadConfig();

        if (properties.isEmpty()) return;

        final String level = properties.getProperty("logup.level");
        if (level == null) return;

        final String logFormatPlaceholder = properties.getProperty("logup.log-format.placeholder");
        final String logFormatDateTimeFormat = properties.getProperty("logup.log-format.date-time-format");
        final String fileDirectory = properties.getProperty("logup.file-recode.directory");
        final String filePlaceholder = properties.getProperty("logup.file-recode.placeholder");
        final String fileDateFormat = properties.getProperty("logup.file-recode.date-format");

        SimpleDateFormat logDateTimeFormatter = null;
        SimpleDateFormat fileNameDateFormatter = null;

        if (logFormatDateTimeFormat != null && !logFormatDateTimeFormat.isEmpty()) {
            try {
                logDateTimeFormatter = new SimpleDateFormat(logFormatDateTimeFormat);

            } catch (IllegalArgumentException e) {
                System.err.println("#your 'logup.log-format.date-time-format' replace value with 'yyyy-MM-dd hh:mm:ss' because IllegalArgument " + e.getMessage());
            }
        }

        if (fileDateFormat != null && !fileDateFormat.isEmpty()) {
            try {
                fileNameDateFormatter = new SimpleDateFormat(fileDateFormat);

            } catch (IllegalArgumentException e) {
                System.err.println("#your 'logup.file-recode.date-format' replace value with 'yyyy-MM-dd' because IllegalArgument " + e.getMessage());
            }
        }


        FileRecorder fileRecord = null;
        if (fileDirectory != null && !fileDirectory.isEmpty()) {
            fileRecord = FileRecorder.builder()
                    .logFileDirectory(fileDirectory)
                    .placeholder(filePlaceholder)
                    .dateFormat(fileNameDateFormatter)
                    .build();
        }

        LogUpFactoryBean.builder()
                .level(level)
                .logFormatter(LogFormatter.builder().dateTimeFormat(logDateTimeFormatter).placeholder(logFormatPlaceholder).build())
                .fileRecorder(fileRecord)
                .start();
    }

    LogUpFactoryBean(final LogFormatter logFormatter, final FileRecorder fileRecorder, final Level level) {
        LogUpFactoryBean.logFormatter = logFormatter;
        LogUpFactoryBean.fileRecorder = fileRecorder;
        LogUpFactoryBean.level = level;
    }

    public static LogUpFactoryBeanBuilder builder() {
        return new LogUpFactoryBeanBuilder();
    }

    public static class LogUpFactoryBeanBuilder {
        private LogFormatter logFormatter;
        private FileRecorder fileRecorder;
        private Level level;

        LogUpFactoryBeanBuilder() {
        }

        /**
         * <p> ex) LogFormatter.getInstance().datetime(string of {@link java.text.SimpleDateFormat}).placeholder({@link LogFormatter.MessagePattern}).build()
         * <p>
         *
         * @param logFormatter {@link LogFormatter}
         * @return {@link LogUpFactoryBean}
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        public LogUpFactoryBeanBuilder logFormatter(final LogFormatter logFormatter) {
            this.logFormatter = logFormatter;
            return this;
        }

        /**
         * <p> ex) FileRecorder.getInstance().logFileDirectory("logs/").dateFormat(string of {@link java.text.SimpleDateFormat}).placeholder({@link FileRecorder.FilePattern}).build();
         * <p>
         *
         * @param fileRecorder {@link FileRecorder}
         * @return {@link LogUpFactoryBean}
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        public LogUpFactoryBeanBuilder fileRecorder(final FileRecorder fileRecorder) {
            this.fileRecorder = fileRecorder;
            return this;
        }

        /**
         * <ul>
         * <li><b>OFF</b>
         * <li><b>ERROR</b> (high)
         * <li><b>WARN</b>
         * <li><b>INFO</b> (row)
         * <li><b>ALL</b>
         * </ul>
         *
         * @param level {@link Level}
         * @return {@link LogUpFactoryBean}
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        public LogUpFactoryBeanBuilder level(final Object level) {
            try {
                this.level = Level.valueOf(String.valueOf(level).toUpperCase());
            } catch (IllegalArgumentException e) {
                this.level = Level.ALL;
            }
            return this;
        }

        public void start() {
            if (LogUpFactoryBean.level != null) return; //TIP 이미 빌드 되있으면 다시 안함

            if (this.logFormatter == null) {
                this.logFormatter = LogFormatter.builder()
                        .placeholder(LogFormatter.MessagePattern.DEFAULT)
                        .dateTimeFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                        .build();


                if (this.fileRecorder != null) {
                    if (fileRecorder.getPlaceholder() == null)
                        fileRecorder.setPlaceholder(FileRecorder.FilePattern.DEFAULT);

                    if (fileRecorder.getLogFileDirectory() == null)
                        fileRecorder.setLogFileDirectory("logs/");

                    if (fileRecorder.getDateFormat() == null)
                        fileRecorder.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
                }
            }

            LogUpFactoryBean.level = this.level;
            LogUpFactoryBean.logFormatter = this.logFormatter;
            LogUpFactoryBean.fileRecorder = this.fileRecorder;
        }
    }
}
