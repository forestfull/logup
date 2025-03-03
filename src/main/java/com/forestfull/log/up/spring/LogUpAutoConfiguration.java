package com.forestfull.log.up.spring;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LogUpProperties.class)
public class LogUpAutoConfiguration {

    @Bean
    LogUpProperties logUpProperties() {
        return new LogUpProperties();
    }
}
