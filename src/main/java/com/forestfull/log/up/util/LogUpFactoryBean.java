package com.forestfull.log.up.util;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.formatter.FileRecorder;
import com.forestfull.log.up.formatter.LogFormatter;
import com.forestfull.log.up.spring.LogUpProperties;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LogUp's general Configuration (LogUp 전역 환경 설정)
 *
 * <ul>
 * <li><b>OFF</b>
 * <li><b>ERROR</b>
 * <li><b>WARN</b>
 * <li><b>TEST</b>
 * <li><b>INFO</b>
 * <li><b>DEBUG</b>
 * <li><b>ALL</b>
 * </ul>
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 * @see Log
 */
public class LogUpFactoryBean {
    static LogUpProperties logUpProperties = null;
    static MessageFormatter[] messageFormatter = null;
    static ExecutorService threadPool = null;

    static {
        initialize();

    }

    protected static void initialize() {
        configureProperties();

        final String[] splitWithDelimiter = MessageFormatter.splitWithDelimiter(LogUpFactoryBean.logUpProperties.getLogFormat().getPlaceholder());
        LogUpFactoryBean.messageFormatter = MessageFormatter.replaceMatchPlaceholder(splitWithDelimiter);
    }

    private static void configureProperties() {
        LogUpProperties logUpProperties;
        try {
            logUpProperties = LogUpConfigLoader.loadConfig();
        } catch (NoClassDefFoundError ignore) {
            logUpProperties = new LogUpProperties();
            LogUpConfigLoader.loggingInitializeManual();
        }

        final Optional<LogFormatter> logFormatOptional = Optional.ofNullable(logUpProperties.getLogFormat());
        final LogFormatter logFormatter = LogFormatter.builder().build(); // TIP: 초기 기본값은 builder에서 지정

        if (logFormatOptional.isPresent()) {
            final LogFormatter logFormat = logFormatOptional.get();
            if (Objects.nonNull(logFormat.getDateTimeFormat())) logFormatter.setDateTimeFormat(logFormat.getDateTimeFormat());
            if (StringUtils.hasText(logFormat.getPlaceholder())) logFormatter.setPlaceholder(logFormat.getPlaceholder());
        }

        final Optional<FileRecorder> fileRecorderOptional = Optional.ofNullable(logUpProperties.getFileRecord());
        FileRecorder fileRecorder = null; // TIP: 초기값은 null(= 선언 안할 시 해당 기능 미제공)

        if (fileRecorderOptional.isPresent()) {
            fileRecorder = FileRecorder.builder().build(); // TIP: 기본값은 builder에서 지정
            FileRecorder fileRecord = fileRecorderOptional.get();

            if (Objects.nonNull(fileRecord.getDateFormat())) fileRecorder.setDateFormat(fileRecord.getDateFormat());
            if (StringUtils.hasText(fileRecord.getDirectory())) fileRecorder.setDirectory(fileRecord.getDirectory());
            if (StringUtils.hasText(fileRecord.getPlaceholder())) fileRecorder.setDirectory(fileRecord.getPlaceholder());
        }

        LogUpFactoryBean.logUpProperties = LogUpFactoryBean.builder()
                .level(logUpProperties.getLevel())
                .poolSize(logUpProperties.getPoolSize())
                .logFormatter(logFormatter)
                .fileRecorder(fileRecorder)
                .start();
    }

    public static LogUpFactoryBeanBuilder builder() {
        return new LogUpFactoryBeanBuilder();
    }

    public static class LogUpFactoryBeanBuilder {
        private LogFormatter logFormatter;
        private FileRecorder fileRecorder;
        private Level level;
        private Integer poolSize;

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
         * <li><b>TEST</b>
         * <li><b>INFO</b>
         * <li><b>DEBUG</b> (row)
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
                Log.LogFactory.builder().message("not a valid level: " + level + System.lineSeparator()).build().run();
                this.level = Level.ALL;
            }
            return this;
        }

        public LogUpFactoryBeanBuilder poolSize(final Integer poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        /**
         * <b>core method</b> of properties's configuration
         */
        public LogUpProperties start() {
            if (LogUpFactoryBean.logUpProperties != null) {
                Log.LogFactory.builder().message("LogUp already started" + System.lineSeparator()).build().run();
                return LogUpFactoryBean.logUpProperties;
            }

            final LogUpProperties properties = new LogUpProperties();
            properties.setLevel(this.level);
            properties.setLogFormat(this.logFormatter);
            properties.setFileRecord(this.fileRecorder);

            if (Objects.nonNull(this.poolSize) && this.poolSize > 0) {
                LogUpFactoryBean.threadPool = Executors.newFixedThreadPool(this.poolSize);

            } else {
                Log.singleLogFactory = Log.LogFactory.builder().build();

            }

            return properties;
        }
    }
}
