package com.forestfull.log.up.util;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * The ConfigLoader class is responsible for loading configuration properties
 * from either an `logup.properties` or `logup.yml` file.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Component
public class LogUpConfigLoader {

    /**
     * Loads the configuration properties from the classpath.
     * It first tries to load from `logup.properties`, if not found, it attempts to load from `logup.yml`.
     *
     * @return The loaded properties.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    static Properties loadConfig() {
        final Properties properties = new Properties();
        final String propertiesFile = "application.properties";
        final String yamlFile = "application.yml";
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(propertiesFile);

        try {
            if (inputStream != null) {
                System.out.println("Loading configuration from " + propertiesFile);
                properties.load(inputStream);
            } else {
                inputStream = classLoader.getResourceAsStream(yamlFile);
                if (inputStream != null) {
                    System.out.println("Loading configuration from " + yamlFile);
                    Map<String, Object> yamlMap = new Yaml().loadAs(inputStream, Map.class);
                    flattenMap("", yamlMap, properties);
                }
            }

            if (inputStream != null) inputStream.close();
        } catch (IOException | NullPointerException e) {
            System.out.println("No configuration file found in classpath: " + e.getMessage());
        }

        return properties;
    }

    /**
     * Recursively flattens a nested map structure into properties format.
     *
     * @param parentKey  The base key for the current level of the map.
     * @param map        The map to flatten.
     * @param properties The properties object to store the flattened key-value pairs.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    private static void flattenMap(String parentKey, Map<String, Object> map, Properties properties) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = parentKey.isEmpty() ? entry.getKey() : parentKey + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                flattenMap(key, (Map<String, Object>) value, properties);
            } else {
                properties.setProperty(key, value.toString());
            }
        }
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