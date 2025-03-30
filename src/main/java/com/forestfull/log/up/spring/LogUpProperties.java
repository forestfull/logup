package com.forestfull.log.up.spring;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.formatter.FileRecorder;
import com.forestfull.log.up.formatter.LogFormatter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "logup")
public class LogUpProperties {
    private Level level;
    @NestedConfigurationProperty
    private LogFormatter logFormat;
    @NestedConfigurationProperty
    private FileRecorder fileRecord;
}