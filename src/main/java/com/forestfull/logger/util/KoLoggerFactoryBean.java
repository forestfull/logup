package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import com.forestfull.logger.config.ConfigLoader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

//    protected static final ExecutorService logConsoleExecutor = Executors.newSingleThreadExecutor();
    protected static LogFormatter logFormatter;
    protected static FileRecorder fileRecorder;
    protected static Level level;
    protected static Boolean jdbc;

    static {
        // TODO: 스프링 연동 작업 필요
        ConfigLoader.getProperty("logFormatter");

        if (KoLoggerFactoryBean.logFormatter == null) KoLoggerFactoryBean.builder().build();
        if (KoLoggerFactoryBean.level == Level.ALL) {
            Log.LogFactory.console("=================================================================================================================================================================" + Log.newLine);
            Log.LogFactory.console(Log.newLine + " # Priority.1 - application.properties" + Log.newLine);
            Log.LogFactory.console("kologger.level=INFO" + Log.newLine);
            Log.LogFactory.console("kologger.jdbc=false" + Log.newLine);
            Log.LogFactory.console("kologger.log-format.placeholder={datetime} [{thread}:{level}] - {msg}{new-line}" + Log.newLine);
            Log.LogFactory.console("kologger.log-format.date-time-format=yyyy-MM-dd HH:mm:ss" + Log.newLine);
            Log.LogFactory.console("kologger.file-recode.directory=log/" + Log.newLine);
            Log.LogFactory.console("kologger.file-recode.placeholder=YOUR_PROJECT_NAME{date}.log" + Log.newLine);
            Log.LogFactory.console("kologger.file-recode.date-format=yyyy-MM-dd" + Log.newLine + Log.newLine);
            Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + Log.newLine);
            Log.LogFactory.console(Log.newLine + " # Priority.2 - application.yml" + Log.newLine);
            Log.LogFactory.console("kologger:" + Log.newLine);
            Log.LogFactory.console("  level: INFO # ALL, INFO, WARN, ERROR, OFF" + Log.newLine);
            Log.LogFactory.console("  jdbc: false # true, false" + Log.newLine);
            Log.LogFactory.console("  log-format:" + Log.newLine);
            Log.LogFactory.console("    placeholder: \"{datetime} [{thread}:{level}] - {msg}{new-line}\"" + Log.newLine);
            Log.LogFactory.console("    date-time-format: yyyy-MM-dd HH:mm:ss" + Log.newLine);
            Log.LogFactory.console("  file-recode:" + Log.newLine);
            Log.LogFactory.console("    directory: log/ # default: log/" + Log.newLine);
            Log.LogFactory.console("    placeholder: YOUR_PROJECT_NAME{date}.log" + Log.newLine);
            Log.LogFactory.console("    date-format: yyyy-MM-dd" + Log.newLine + Log.newLine);
            Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + Log.newLine);
            Log.LogFactory.console(Log.newLine + " # Priority.3 - source code" + Log.newLine);
            Log.LogFactory.console("KoLoggerFactoryBean.builder()" + Log.newLine);
            Log.LogFactory.console("                    .level(Level.INFO)" + Log.newLine);
            Log.LogFactory.console("                    .jdbc(true)" + Log.newLine);
            Log.LogFactory.console("                    .logFormatter(LogFormatter.getInstance().placeholder(\"{datetime} [{thread}:{level}] - {msg}{new-line}\").datetime(\"yyyy-MM-dd HH:mm:ss\").)" + Log.newLine);
            Log.LogFactory.console("                    .fileRecorder(FileRecorder.getInstance().placeholder(\"YOUR_PROJECT_NAME{date}.log\").logFileDirectory(\"logs/\").dateFormat(\"yyyy-MM-dd\"))" + Log.newLine);
            Log.LogFactory.console("                    .build();" + Log.newLine + Log.newLine);
            Log.LogFactory.console("=================================================================================================================================================================" + Log.newLine);
        }
    }

    KoLoggerFactoryBean(final LogFormatter logFormatter, final FileRecorder fileRecorder, final Level level, final Boolean jdbc) {
        KoLoggerFactoryBean.logFormatter = logFormatter;
        KoLoggerFactoryBean.fileRecorder = fileRecorder;
        KoLoggerFactoryBean.level = level;
        KoLoggerFactoryBean.jdbc = jdbc;
    }

    public static KoLoggerFactoryBeanBuilder builder() {
        return new KoLoggerFactoryBeanBuilder();
    }

    public static class KoLoggerFactoryBeanBuilder {
        private LogFormatter logFormatter;
        private FileRecorder fileRecorder;
        private Level level;
        private Boolean jdbc;

        KoLoggerFactoryBeanBuilder() {
        }

        public KoLoggerFactoryBeanBuilder logFormatter(final LogFormatter logFormatter) {
            this.logFormatter = logFormatter;
            return this;
        }

        public KoLoggerFactoryBeanBuilder fileRecorder(final FileRecorder fileRecorder) {
            this.fileRecorder = fileRecorder;
            return this;
        }

        public KoLoggerFactoryBeanBuilder level(final Level level) {
            this.level = level;
            return this;
        }

        public KoLoggerFactoryBeanBuilder jdbc(final Boolean jdbc) {
            this.jdbc = jdbc;
            return this;
        }

        public KoLoggerFactoryBean build() {
            this.level = level == null ? Level.ALL : level;
            this.jdbc = jdbc != null;

            if (logFormatter == null)
                this.logFormatter = LogFormatter.getInstance();

			if (this.logFormatter.getPlaceholder() == null)
                this.logFormatter.placeholder(LogFormatter.MessagePattern.DEFAULT);

			if (this.logFormatter.getDateTimeFormat() == null)
                this.logFormatter.datetime("yyyy-MM-dd HH:mm:ss");

            if (fileRecorder != null) {
                if (fileRecorder.getPlaceholder() == null)
                    fileRecorder.placeholder(FileRecorder.FilePattern.DEFAULT);

                if (fileRecorder.getLogFileDirectory() == null)
                    fileRecorder.logFileDirectory("logs/");

                if (fileRecorder.getDateFormat() == null)
                    fileRecorder.dateFormat("yyyy-MM-dd");
            }

            return new KoLoggerFactoryBean(this.logFormatter, this.fileRecorder, this.level, false);
        }
    }
}
