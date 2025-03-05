package com.forestfull.log.up.util;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.formatter.FileRecorder;
import com.forestfull.log.up.formatter.LogFormatter;
import com.forestfull.log.up.spring.LogUpProperties;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

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
    static Level level;
    static MessageFormatter[] messageFormatter;
    static LogFormatter logFormatter;
    static FileRecorder fileRecorder;

    static {
        initialize();
    }

    protected static void initialize() {
        configureProperties();
        fixIncorrectProperties();

        final String[] splitWithDelimiter = MessageFormatter.splitWithDelimiter(logFormatter.getPlaceholder());
        LogUpFactoryBean.messageFormatter = MessageFormatter.replaceMatchPlaceholder(splitWithDelimiter);
    }

    private static void fixIncorrectProperties() {
        if (LogUpFactoryBean.level == null) {
            LogUpConfigLoader.loggingInitializeManual();
            LogUpFactoryBean.level = Level.ALL;
        }

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
        LogUpProperties logUpProperties = null;

        try {
            logUpProperties = LogUpConfigLoader.loadConfig();

        } catch (NoClassDefFoundError ignore) {}

        if (logUpProperties == null) return;

        Level level = logUpProperties.getLevel();
        if (level == null) return;
        if (logUpProperties.getLogFormat() == null) return;

        final String logFormatPlaceholder = logUpProperties.getLogFormat().getPlaceholder();
        final SimpleDateFormat logFormatDateTimeFormat = logUpProperties.getLogFormat().getDateTimeFormat();

        FileRecorder fileRecord = logUpProperties.getFileRecord();
        if (fileRecord != null && fileRecord.getDateFormat() != null) {
            fileRecord = FileRecorder.builder()
                    .dateFormat(fileRecord.getDateFormat())
                    .build();

            if (StringUtils.hasText(fileRecord.getDirectory())) fileRecord.setDirectory(fileRecord.getDirectory());
            if (StringUtils.hasText(fileRecord.getPlaceholder())) fileRecord.setDirectory(fileRecord.getPlaceholder());
        }

        LogUpFactoryBean.builder()
                .level(level)
                .logFormatter(LogFormatter.builder().dateTimeFormat(logFormatDateTimeFormat).placeholder(logFormatPlaceholder).build())
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

            LogUpFactoryBean.level = this.level;
            LogUpFactoryBean.logFormatter = this.logFormatter;
            LogUpFactoryBean.fileRecorder = this.fileRecorder;
            ;
        }
    }
}
