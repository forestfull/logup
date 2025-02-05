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
        if (KoLoggerFactoryBean.level == null) {
//            TODO: properties, yml 설명을 써주자
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
