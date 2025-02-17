package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
 * @since JDK 1.8
 */

public class KoLoggerFactoryBean {
    static LogFormatter logFormatter;
    static FileRecorder fileRecorder;
    static Level level;

    static {
        configureProperties();

        if (KoLoggerFactoryBean.level == null)
            loggingInitializeManual();

        defaultInitialize();

        if (isSpringPresent()){
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(com.forestfull.logger.spring.ObservableConfig.class);
            context.scan(System.getProperty("base.package"));
            context.refresh();
        }
    }

    private static boolean isSpringPresent() {
        try {
            Class.forName("org.springframework.context.annotation.AnnotationConfigApplicationContext");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static void loggingInitializeManual() {
        Log.LogFactory.console(Log.newLine + "=================================================================================================================================================================" + Log.newLine);
        Log.LogFactory.console("KoLogger Setting Example");
        Log.LogFactory.console(Log.newLine + "=================================================================================================================================================================" + Log.newLine);
        Log.LogFactory.console(Log.newLine + " # Priority.1 - application.properties" + Log.newLine);
        Log.LogFactory.console("kologger.level=INFO" + Log.newLine);
        Log.LogFactory.console("kologger.jdbc=true" + Log.newLine);
        Log.LogFactory.console("kologger.log-format.placeholder={datetime} {thread} {level} - {msg}{new-line}" + Log.newLine);
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
        Log.LogFactory.console("    placeholder: \"{datetime} {thread} {level} - {msg}{new-line}\"" + Log.newLine);
        Log.LogFactory.console("    date-time-format: yyyy-MM-dd HH:mm:ss" + Log.newLine);
        Log.LogFactory.console("  file-recode:" + Log.newLine);
        Log.LogFactory.console("    directory: log/" + Log.newLine);
        Log.LogFactory.console("    placeholder: YOUR_PROJECT_NAME{date}.log" + Log.newLine);
        Log.LogFactory.console("    date-format: yyyy-MM-dd" + Log.newLine + Log.newLine);
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + Log.newLine);
        Log.LogFactory.console(Log.newLine + " # Priority.3 - source code" + Log.newLine);
        Log.LogFactory.console("KoLoggerFactoryBean.builder()" + Log.newLine);
        Log.LogFactory.console("                    .level(Level.INFO)" + Log.newLine);
        Log.LogFactory.console("                    .logFormatter(LogFormatter.builder().build())" + Log.newLine);
        Log.LogFactory.console("                    .fileRecorder(FileRecorder.builder().logFileDirectory(\"logs/\").build())" + Log.newLine);
        Log.LogFactory.console("                    .build();" + Log.newLine + Log.newLine);
        Log.LogFactory.console("=================================================================================================================================================================" + Log.newLine + Log.newLine);
    }

    private static void defaultInitialize() {
        if (KoLoggerFactoryBean.level == null)
            KoLoggerFactoryBean.level = Level.ALL;

        if (KoLoggerFactoryBean.logFormatter == null)
            KoLoggerFactoryBean.logFormatter = LogFormatter.builder().build();

        if (KoLoggerFactoryBean.logFormatter.getPlaceholder() == null)
            KoLoggerFactoryBean.logFormatter.setPlaceholder(LogFormatter.getDefaultPlaceHolder());

        if (KoLoggerFactoryBean.logFormatter.getDateTimeFormat() == null)
            KoLoggerFactoryBean.logFormatter.setDateTimeFormat(LogFormatter.getDefaultDateTimeFormat());

        if (KoLoggerFactoryBean.fileRecorder == null) return;

        if (KoLoggerFactoryBean.fileRecorder.getPlaceholder() == null)
            KoLoggerFactoryBean.fileRecorder.setPlaceholder(FileRecorder.getDefaultPlaceHolder());

        if (KoLoggerFactoryBean.fileRecorder.getDateFormat() == null)
            KoLoggerFactoryBean.fileRecorder.setDateFormat(FileRecorder.getDefaultDateFormat());
    }

    private static void configureProperties() {
        final Properties properties = ConfigLoader.loadConfig();

        if (properties.isEmpty()) return;

        final String level = properties.getProperty("kologger.level");
        if (level == null) return;

        final String logFormatPlaceholder = properties.getProperty("kologger.log-format.placeholder");
        final String logFormatDateTimeFormat = properties.getProperty("kologger.log-format.date-time-format");
        final String fileDirectory = properties.getProperty("kologger.file-recode.directory");
        final String filePlaceholder = properties.getProperty("kologger.file-recode.placeholder");
        final String fileDateFormat = properties.getProperty("kologger.file-recode.date-format");

        SimpleDateFormat logDateTimeFormatter = null;
        SimpleDateFormat fileNameDateFormatter = null;

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
                    .placeholder(filePlaceholder)
                    .dateFormat(fileNameDateFormatter)
                    .build();
        }

        KoLoggerFactoryBean.builder()
                .level(level)
                .logFormatter(LogFormatter.builder().dateTimeFormat(logDateTimeFormatter).placeholder(logFormatPlaceholder).build())
                .fileRecorder(fileRecord)
                .start();
    }

    KoLoggerFactoryBean(final LogFormatter logFormatter, final FileRecorder fileRecorder, final Level level) {
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
         * @return {@link KoLoggerFactoryBean}
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
         * @return {@link KoLoggerFactoryBean}
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
         * @return {@link KoLoggerFactoryBean}
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
