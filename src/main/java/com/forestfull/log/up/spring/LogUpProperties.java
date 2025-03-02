package com.forestfull.log.up.spring;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.util.FileRecorder;
import com.forestfull.log.up.util.LogFormatter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "logup")
public class LogUpProperties {
    private Level level;
    private LogFormatter logFormat;
    private FileRecorder fileRecord;
}