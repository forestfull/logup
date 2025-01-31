package com.forestfull.logger.util;

import com.forestfull.logger.Level;

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

public class KoLoggerFactoryBean implements Cloneable {

//    protected static final ExecutorService logConsoleExecutor = Executors.newCachedThreadPool();
    private final LogFormatter logFormatter;
    private final FileRecorder fileRecorder;
    private final Level level;
    private final Boolean jdbc;

    KoLoggerFactoryBean(final LogFormatter logFormatter, final FileRecorder fileRecorder, final Level level, final Boolean jdbc) {
        this.logFormatter = logFormatter;
        this.fileRecorder = fileRecorder;
        this.level = level;
        this.jdbc = jdbc;
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

        public String toString() {
            String var10000 = String.valueOf(this.logFormatter);
            return "KoLoggerFactoryBean.KoLoggerFactoryBeanBuilder(logFormatter=" + var10000 + ", fileRecorder=" + String.valueOf(this.fileRecorder) + ", level=" + String.valueOf(this.level) + ")";
        }
    }

    protected LogFormatter getLogFormatter() {
        return logFormatter;
    }

    protected FileRecorder getFileRecorder() {
        return fileRecorder;
    }

    protected Level getLevel() {
        return level;
    }

    protected Boolean getJdbc() {
        return jdbc;
    }

    public KoLoggerFactoryBean clone() {
        try {
            return (KoLoggerFactoryBean) super.clone();
        } catch (CloneNotSupportedException ignore) {
        }
        return null;
    }
}
