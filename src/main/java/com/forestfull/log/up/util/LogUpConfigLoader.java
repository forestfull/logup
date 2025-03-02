package com.forestfull.log.up.util;

import com.forestfull.log.up.spring.LogUpProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class LogUpConfigLoader {

    private static final String[] LOCATIONS = {
            "config/application.properties",
            "config/application.yaml",
            "config/application.yml",
            "application.properties",
            "application.yaml",
            "application.yml"
    };

    public static LogUpProperties loadConfig() {
        // Spring 환경 설정 초기화
        StandardEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        String activeProfile = environment.getActiveProfiles()[0];
        activeProfile = activeProfile != null ? activeProfile.trim() : null;

        // 설정 파일 로드
        injectPropertySources(activeProfile, propertySources);

        return Binder.get(environment).bind("logup", Bindable.of(LogUpProperties.class)).orElse(null);
    }

    private static void injectPropertySources(String activeProfile, MutablePropertySources propertySources) {
        final String[] locations = Arrays.stream(LOCATIONS).flatMap(loc -> {
            if (StringUtils.hasText(activeProfile))
                return Stream.of(loc.replace("application.", "application-" + activeProfile.toLowerCase() + "."), loc);

            return Stream.of(loc);
        }).toArray(String[]::new);

        for (String location : locations) {
            PropertySource<?> source = getPropertySources(activeProfile, location);
            if (source != null) {
                propertySources.addLast(source);
                final String activeProfileUnderVersion = String.valueOf(source.getProperty("spring.profiles"));
                final String activeProfileCurrentVersion = String.valueOf(source.getProperty("spring.config.activate.on-profile"));
                if (activeProfile != null && (activeProfile.equalsIgnoreCase(activeProfileUnderVersion) || activeProfile.equalsIgnoreCase(activeProfileCurrentVersion)))
                    break;
            }
        }
    }

    private static PropertySource<?> getPropertySources(String activeProfile, String location) {
        final Resource resource = new ClassPathResource(location);
        PropertySource<?> propertySource = null;
        if (resource.exists()) {
            try {
                if (location.endsWith(".yaml") || location.endsWith(".yml")) {
                    final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
                    final List<PropertySource<?>> yamlSources = loader.load(location, resource);
                    for (PropertySource<?> source : yamlSources) {
                        if (!source.containsProperty("logup.level")) continue;

                        final String activeProfileUnderVersion = String.valueOf(source.getProperty("spring.profiles"));
                        final String activeProfileCurrentVersion = String.valueOf(source.getProperty("spring.config.activate.on-profile"));
                        if (activeProfileUnderVersion == null && activeProfileCurrentVersion == null) {
                            propertySource = source;

                        } else if (activeProfile != null && (activeProfile.equalsIgnoreCase(activeProfileUnderVersion) || activeProfile.equalsIgnoreCase(activeProfileCurrentVersion))) {
                            propertySource = source;
                            return propertySource;

                        }
                    }

                } else {
                    propertySource = new ResourcePropertySource(location, resource);
                }
            } catch (IOException ignored) {
            }
        }
        return propertySource;
    }


    public static void loggingInitializeManual() {
        Log.LogFactory.console(System.lineSeparator() + "=================================================================================================================================================================" + System.lineSeparator());
        Log.LogFactory.console("LogUp Setting Example");
        Log.LogFactory.console(System.lineSeparator() + "=================================================================================================================================================================" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.1 - classpath: application.properties" + System.lineSeparator());
        Log.LogFactory.console("logup.level=INFO" + System.lineSeparator());
        Log.LogFactory.console("logup.log-format.placeholder={datetime} {level} {thread} - {msg}{new-line}" + System.lineSeparator());
        Log.LogFactory.console("logup.log-format.date-time-format=yyyy-MM-dd HH:mm:ss" + System.lineSeparator());
        Log.LogFactory.console("logup.file-recode.directory=log/" + System.lineSeparator());
        Log.LogFactory.console("logup.file-recode.placeholder=" + FileRecorder.FilePattern.PROJECT_NAME + "-{date}.log" + System.lineSeparator());
        Log.LogFactory.console("logup.file-recode.date-format=yyyy-MM-dd" + System.lineSeparator() + System.lineSeparator());
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.2 - classpath: application.yml" + System.lineSeparator());
        Log.LogFactory.console("logup:" + System.lineSeparator());
        Log.LogFactory.console("  level: INFO # ALL, INFO, WARN, ERROR, OFF" + System.lineSeparator());
        Log.LogFactory.console("  log-format:" + System.lineSeparator());
        Log.LogFactory.console("    placeholder: \"{datetime} {level} {thread} - {msg}{new-line}\"" + System.lineSeparator());
        Log.LogFactory.console("    date-time-format: yyyy-MM-dd HH:mm:ss" + System.lineSeparator());
        Log.LogFactory.console("  file-recode:" + System.lineSeparator());
        Log.LogFactory.console("    directory: log/ # is default" + System.lineSeparator());
        Log.LogFactory.console("    placeholder: " + FileRecorder.FilePattern.PROJECT_NAME + "-{date}.log" + System.lineSeparator());
        Log.LogFactory.console("    date-format: yyyy-MM-dd" + System.lineSeparator() + System.lineSeparator());
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.3 - source code" + System.lineSeparator());
        Log.LogFactory.console("LogUpFactoryBean.builder()" + System.lineSeparator());
        Log.LogFactory.console("                    .level(Level.INFO)" + System.lineSeparator());
        Log.LogFactory.console("                    .logFormatter(LogFormatter.builder().build())" + System.lineSeparator());
        Log.LogFactory.console("                    .fileRecorder(FileRecorder.builder().logFileDirectory(\"logs/\").build())" + System.lineSeparator());
        Log.LogFactory.console("                    .build();" + System.lineSeparator() + System.lineSeparator());
        Log.LogFactory.console("=================================================================================================================================================================" + System.lineSeparator() + System.lineSeparator());
    }
}