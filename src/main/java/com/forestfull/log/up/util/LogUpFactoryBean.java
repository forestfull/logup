package com.forestfull.log.up.util;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.formatter.FileRecorder;
import com.forestfull.log.up.formatter.LogFormatter;
import com.forestfull.log.up.spring.LogUpProperties;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

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
    static LogUpProperties logUpProperties;
    static MessageFormatter[] messageFormatter;

    static {
        initialize();
    }

    protected static void initialize() {
        if (configureProperties() == null) {
            LogUpConfigLoader.loggingInitializeManual();

        } else {
            final String[] splitWithDelimiter = MessageFormatter.splitWithDelimiter(logUpProperties.getLogFormat().getPlaceholder());
            LogUpFactoryBean.messageFormatter = MessageFormatter.replaceMatchPlaceholder(splitWithDelimiter);

        }
    }

    private static LogUpProperties configureProperties() {
        try {
            LogUpFactoryBean.logUpProperties = LogUpConfigLoader.loadConfig();
        } catch (NoClassDefFoundError ignore) {
        }

        if (LogUpFactoryBean.logUpProperties == null) return null;

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

        LogUpFactoryBean.builder()
                .level(logUpProperties.getLevel())
                .poolSize(logUpProperties.getPoolSize())
                .logFormatter(logFormatter)
                .fileRecorder(fileRecorder)
                .start();

        return LogUpFactoryBean.logUpProperties;
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
                // TODO 에러 처리 메시지
                this.level = Level.ALL;
            }
            return this;
        }

        public LogUpFactoryBeanBuilder poolSize(final int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        /**
         * <b>core method</b> of properties's configuration
         */
        public void start() {
            if (LogUpFactoryBean.logUpProperties != null){
                // TIP: 이미 빌드 되있으면 다시 안함
                // TODO 에러 처리 메시지
                return;
            }

            final LogUpProperties properties = new LogUpProperties();
            properties.setLevel(this.level);
            properties.setPoolSize(this.poolSize);
            properties.setLogFormat(this.logFormatter);
            properties.setFileRecord(this.fileRecorder);

            LogUpFactoryBean.logUpProperties = properties;
        }
    }
}
