package com.forestfull.log.up.spring;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.util.FileRecorder;
import com.forestfull.log.up.util.LogFormatter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
@ConfigurationProperties(prefix = "logup")
public class LogUpProperties {
    private Level level;
    private LogFormatter logFormat;
    private FileRecorder fileRecord;

    public Properties toProperties() {
        Properties properties = new Properties();

        if (level != null) {
            properties.setProperty("logup.level", level.name());
        }

        if (logFormat != null) {
            properties.setProperty("logup.log-format.placeholder", logFormat.getPlaceholder());
            properties.setProperty("logup.log-format.date-time-format",
                    logFormat.getDateTimeFormat() != null ? logFormat.getDateTimeFormat().toPattern() : "");
        }

        if (fileRecord != null) {
            properties.setProperty("logup.file-record.placeholder", fileRecord.getPlaceholder());
            properties.setProperty("logup.file-record.date-format",
                    fileRecord.getDateFormat() != null ? fileRecord.getDateFormat().toPattern() : "");
            properties.setProperty("logup.file-record.log-file-directory", fileRecord.getLogFileDirectory());
        }

        return properties;
    }
}