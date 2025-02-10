package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import com.forestfull.logger.config.ConfigLoader;

import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * KoLooger's general Configuration (KoLooger 전역 환경 설정)
 * <p><b>This class's instance must be initialized only once</b>
 * <br>(이 클래스의 인스턴스는 한 번만 초기화하여야 합니다.)</p>
 *
 * <ul>
 * <li><b>OFF</b>
 * <li><b>ERROR</b>
 * <li><b>WARN</b>
 * <li><b>INFO</b>
 * <li><b>ALL</b>
 * </ul>
 *
 * @author Vigfoot
 * @version 1.0
 * @since JDK 1.6
 */

public class KoLoggerFactoryBean {
    protected static LogFormatter logFormatter;
    protected static FileRecorder fileRecorder;
    protected static Level level;

    static {
        configureProperties();
        if (KoLoggerFactoryBean.level == null)
            loggingInitializeManual();
    }

    private static void loggingInitializeManual() {
        Log.LogFactory.console(Log.newLine + "=================================================================================================================================================================" + Log.newLine);
        Log.LogFactory.console("KoLogger Setting Example");
        Log.LogFactory.console(Log.newLine + "=================================================================================================================================================================" + Log.newLine);
        Log.LogFactory.console(Log.newLine + " # Priority.1 - application.properties" + Log.newLine);
        Log.LogFactory.console("kologger.level=INFO" + Log.newLine);
        Log.LogFactory.console("kologger.jdbc=true" + Log.newLine);
        Log.LogFactory.console("kologger.log-format.placeholder={datetime} [{thread}:{level}] - {msg}{new-line}" + Log.newLine);
        Log.LogFactory.console("kologger.log-format.date-time-format=yyyy-MM-dd HH:mm:ss" + Log.newLine);
        Log.LogFactory.console("kologger.file-recode.directory=log/" + Log.newLine);
        Log.LogFactory.console("kologger.file-recode.placeholder=YOUR_PROJECT_NAME{date}.log" + Log.newLine);
        Log.LogFactory.console("kologger.file-recode.date-format=yyyy-MM-dd" + Log.newLine + Log.newLine);
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + Log.newLine);
        Log.LogFactory.console(Log.newLine + " # Priority.2 - application.yml" + Log.newLine);
        Log.LogFactory.console("kologger:" + Log.newLine);
        Log.LogFactory.console("  level: INFO # ALL, INFO, WARN, ERROR, OFF" + Log.newLine);
        Log.LogFactory.console("  jdbc: true # true, false" + Log.newLine);
        Log.LogFactory.console("  log-format:" + Log.newLine);
        Log.LogFactory.console("    placeholder: \"{datetime} [{thread}:{level}] - {msg}{new-line}\"" + Log.newLine);
        Log.LogFactory.console("    date-time-format: yyyy-MM-dd HH:mm:ss" + Log.newLine);
        Log.LogFactory.console("  file-recode:" + Log.newLine);
        Log.LogFactory.console("    directory: log/" + Log.newLine);
        Log.LogFactory.console("    placeholder: YOUR_PROJECT_NAME{date}.log" + Log.newLine);
        Log.LogFactory.console("    date-format: yyyy-MM-dd" + Log.newLine + Log.newLine);
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + Log.newLine);
        Log.LogFactory.console(Log.newLine + " # Priority.3 - source code" + Log.newLine);
        Log.LogFactory.console("KoLoggerFactoryBean.builder()" + Log.newLine);
        Log.LogFactory.console("                    .level(Level.INFO)" + Log.newLine);
        Log.LogFactory.console("                    .jdbc(true)" + Log.newLine);
        Log.LogFactory.console("                    .logFormatter(LogFormatter.builder().placeholder(\"{datetime} [{thread}:{level}] - {msg}{new-line}\").datetime(\"yyyy-MM-dd HH:mm:ss\").build())" + Log.newLine);
        Log.LogFactory.console("                    .fileRecorder(FileRecorder.builder().placeholder(\"YOUR_PROJECT_NAME{date}.log\").logFileDirectory(\"logs/\").dateFormat(\"yyyy-MM-dd\").build())" + Log.newLine);
        Log.LogFactory.console("                    .build();" + Log.newLine + Log.newLine);
        Log.LogFactory.console("=================================================================================================================================================================" + Log.newLine + Log.newLine);
    }

    private static void configureProperties() {
        final Properties properties = ConfigLoader.loadConfig();

        if (properties.isEmpty()) return;

        final String level = properties.getProperty("kologger.level");
        final String logFormatPlaceholder = properties.getProperty("kologger.log-format.placeholder");
        final String logFormatDateTimeFormat = properties.getProperty("kologger.log-format.date-time-format");
        final String fileDirectory = properties.getProperty("kologger.file-recode.directory");
        final String filePlaceholder = properties.getProperty("kologger.file-recode.placeholder");
        final String fileDateFormat = properties.getProperty("kologger.file-recode.date-format");

        SimpleDateFormat logDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat fileNameDateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        if (logFormatDateTimeFormat != null && !logFormatDateTimeFormat.isEmpty()) {
            try {
                logDateTimeFormatter = new SimpleDateFormat(logFormatDateTimeFormat);

            } catch (IllegalArgumentException e) {
                System.err.println("#your 'kologger.log-format.date-time-format' replace value with 'yyyy-MM-dd hh:mm:ss' because IllegalArgument " + e.getMessage());
            }
        }

        if (fileDateFormat != null && !fileDateFormat.isEmpty()) {
            try {
                fileNameDateFormatter = new SimpleDateFormat(fileDateFormat);

            } catch (IllegalArgumentException e) {
                System.err.println("#your 'kologger.file-recode.date-format' replace value with 'yyyy-MM-dd' because IllegalArgument " + e.getMessage());
            }
        }


        FileRecorder fileRecord = null;
        if (fileDirectory != null && !fileDirectory.isEmpty()) {
            fileRecord = FileRecorder.builder()
                    .logFileDirectory(fileDirectory)
                    .dateFormat(fileNameDateFormatter)
                    .build();

            if (filePlaceholder != null && !filePlaceholder.isEmpty()) {
                fileRecord.setPlaceholder(filePlaceholder);
            }
        }


        LogFormatter logFormat = LogFormatter.builder().dateTimeFormat(logDateTimeFormatter).build();

        if (logFormat.getPlaceholder() != null) {
            logFormat.setPlaceholder(LogFormatter.MessagePattern.DEFAULT);
        }

        KoLoggerFactoryBean.builder()
                .level(level)
                .logFormatter(logFormat)
                .fileRecorder(fileRecord)
                .start();
    }

    KoLoggerFactoryBean(final LogFormatter logFormatter, final FileRecorder fileRecorder, final Level level, final Boolean jdbc) {
        KoLoggerFactoryBean.logFormatter = logFormatter;
        KoLoggerFactoryBean.fileRecorder = fileRecorder;
        KoLoggerFactoryBean.level = level;
    }

    public static KoLoggerFactoryBeanBuilder builder() {
        return new KoLoggerFactoryBeanBuilder();
    }

    public static class KoLoggerFactoryBeanBuilder {
        private LogFormatter logFormatter;
        private FileRecorder fileRecorder;
        private Level level;

        KoLoggerFactoryBeanBuilder() {
        }

        /**
         * <p> ex) LogFormatter.getInstance().datetime(string of {@link java.text.SimpleDateFormat}).placeholder({@link LogFormatter.MessagePattern}).build()
         * <p>
         *
         * @param logFormatter {@link LogFormatter}
         */
        public KoLoggerFactoryBeanBuilder logFormatter(final LogFormatter logFormatter) {
            this.logFormatter = logFormatter;
            return this;
        }

        /**
         * <p> ex) FileRecorder.getInstance().logFileDirectory("logs/").dateFormat(string of {@link java.text.SimpleDateFormat}).placeholder({@link FileRecorder.FilePattern}).build();
         * <p>
         *
         * @param fileRecorder {@link FileRecorder}
         */
        public KoLoggerFactoryBeanBuilder fileRecorder(final FileRecorder fileRecorder) {
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
         */
        public KoLoggerFactoryBeanBuilder level(final Object level) {
            try {
                this.level = Level.valueOf(String.valueOf(level).toUpperCase());
            } catch (IllegalArgumentException e) {
                this.level = Level.ALL;
            }
            return this;
        }

        public void start() {
            if (KoLoggerFactoryBean.level != null) return; //TIP 이미 빌드 되있으면 다시 안함

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

            KoLoggerFactoryBean.level = this.level;
            KoLoggerFactoryBean.logFormatter = this.logFormatter;
            KoLoggerFactoryBean.fileRecorder = this.fileRecorder;
        }
    }
}
