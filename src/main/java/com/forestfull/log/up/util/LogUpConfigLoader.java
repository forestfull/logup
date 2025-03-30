package com.forestfull.log.up.util;

import com.forestfull.log.up.formatter.FileRecorder;
import com.forestfull.log.up.formatter.LogFormatter;
import com.forestfull.log.up.spring.LogUpProperties;
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

    /**
     * Loads the LogUpProperties configuration by initializing the Spring environment
     * and binding the properties from the configuration files.
     *
     * @return LogUpProperties object with the loaded configuration or null if binding fails
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     * @throws NoClassDefFoundError "StandardEnvironment"
     */
    public static LogUpProperties loadConfig() throws NoClassDefFoundError {
        // Spring 환경 설정 초기화
        StandardEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        String activeProfile = environment.getActiveProfiles().length == 0 ? null : environment.getActiveProfiles()[0];
        activeProfile = activeProfile != null ? activeProfile.trim() : null;

        // 설정 파일 로드
        injectPropertySources(activeProfile, propertySources);

        return Binder.get(environment).bind("logup", Bindable.of(LogUpProperties.class)).orElse(null);
    }

    /**
     * Injects property sources into the MutablePropertySources based on the active profile.
     *
     * @param activeProfile   the currently active profile, if any
     * @param propertySources the MutablePropertySources to inject the property sources into
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    private static void injectPropertySources(String activeProfile, MutablePropertySources propertySources) {
        String[] locations;

        if (StringUtils.hasText(activeProfile)) {
            String[] customProps = Arrays.stream(LOCATIONS).map(loc -> loc.replace("application.", "application-" + activeProfile.toLowerCase() + ".")).toArray(String[]::new);
            locations = Stream.of(customProps, LOCATIONS).flatMap(Stream::of).toArray(String[]::new);
        } else {
            locations = LOCATIONS;
        }

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

    /**
     * Loads a PropertySource from the specified location.
     *
     * @param activeProfile the currently active profile, if any
     * @param location      the location of the property source to load
     * @return the loaded PropertySource, or null if the source does not exist
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
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
                        if (activeProfile != null && (activeProfile.equalsIgnoreCase(activeProfileUnderVersion) || activeProfile.equalsIgnoreCase(activeProfileCurrentVersion))) {
                            propertySource = source;
                            return propertySource;

                        } else if (propertySource == null) {
                            propertySource = source;

                        }
                    }

                    return propertySource;

                } else {
                    propertySource = new ResourcePropertySource(location, resource);
                }
            } catch (IOException ignored) {
            }
        }
        return propertySource;
    }


    static void loggingInitializeManual() {
        Log.LogFactory.console(System.lineSeparator() + "=================================================================================================================================================================" + System.lineSeparator());
        Log.LogFactory.console("LogUp Setting Example");
        Log.LogFactory.console(System.lineSeparator() + "=================================================================================================================================================================" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.1 - classpath: application.properties or (config/application.properties)" + System.lineSeparator());
        Log.LogFactory.console("logup.level=INFO" + System.lineSeparator());
        Log.LogFactory.console("logup.log-format.placeholder=" + LogFormatter.MessagePattern.DEFAULT + System.lineSeparator());
        Log.LogFactory.console("logup.log-format.date-time-format=" + LogFormatter.getDefaultDateTimeFormat() + System.lineSeparator());
        Log.LogFactory.console("logup.file-record.directory=log/" + System.lineSeparator());
        Log.LogFactory.console("logup.file-record.placeholder=" + FileRecorder.getDefaultPlaceHolder() + System.lineSeparator());
        Log.LogFactory.console("logup.file-record.date-format=" + FileRecorder.getDefaultDateFormat() + System.lineSeparator() + System.lineSeparator());
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.2 - classpath: application.yml or (config/application.yml, config/application.yaml, application.yaml) " + System.lineSeparator());
        Log.LogFactory.console("logup:" + System.lineSeparator());
        Log.LogFactory.console("  level: INFO # ALL, DEBUG, INFO, TEST(Plain Text), WARN, ERROR, OFF" + System.lineSeparator());
        Log.LogFactory.console("  log-format:" + System.lineSeparator());
        Log.LogFactory.console("    placeholder: \"" + LogFormatter.MessagePattern.DEFAULT + "\"" + System.lineSeparator());
        Log.LogFactory.console("    date-time-format: " + LogFormatter.getDefaultDateTimeFormat() + System.lineSeparator());
        Log.LogFactory.console("  file-record:" + System.lineSeparator());
        Log.LogFactory.console("    directory: log/ # is default" + System.lineSeparator());
        Log.LogFactory.console("    placeholder: " + FileRecorder.getDefaultPlaceHolder() + System.lineSeparator());
        Log.LogFactory.console("    date-format: " + FileRecorder.getDefaultDateFormat() + System.lineSeparator() + System.lineSeparator());
        Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + System.lineSeparator());
        Log.LogFactory.console(System.lineSeparator() + " # Priority.3 - source code" + System.lineSeparator());
        Log.LogFactory.console("LogUpFactoryBean.builder()" + System.lineSeparator());
        Log.LogFactory.console("                    .level(Level.INFO)" + System.lineSeparator());
        Log.LogFactory.console("                    .logFormatter(LogFormatter.builder().build())" + System.lineSeparator());
        Log.LogFactory.console("                    .fileRecorder(FileRecorder.builder().build())" + System.lineSeparator());
        Log.LogFactory.console("                    .build();" + System.lineSeparator() + System.lineSeparator());
    }
}