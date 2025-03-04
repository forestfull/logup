package com.forestfull.log.up.spring;

import com.forestfull.log.up.formatter.FileRecorder;
import com.forestfull.log.up.formatter.LogFormatter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({LogUpProperties.class, LogFormatter.class, FileRecorder.class})
public class LogUpAutoConfiguration {

    @Bean
    public LogUpProperties logUpProperties() {
        return new LogUpProperties();
    }

    @Bean
    public LogFormatter logFormat() {
        return new LogFormatter();
    }

    @Bean
    public FileRecorder fileRecord() {
        return new FileRecorder();
    }
}
