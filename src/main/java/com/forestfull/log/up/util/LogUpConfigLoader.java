package com.forestfull.log.up.util;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Configuration loader that supports both YAML (.yml) and Properties (.properties) files.
 * It follows the same priority order as Spring Boot:
 * <ol>
 *     <li>config/application.properties</li>
 *     <li>config/application.yml</li>
 *     <li>application.properties</li>
 *     <li>application.yml</li>
 * </ol>
 *
 * If no configuration file is found, an exception is thrown.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Component
public class LogUpConfigLoader {
    /**
     * Loads the configuration properties from available files.
     * It checks files in the following order:
     * <ul>
     *     <li>config/application.properties</li>
     *     <li>config/application.yml</li>
     *     <li>application.properties</li>
     *     <li>application.yml</li>
     * </ul>
     *
     * If none of these files exist, an exception is thrown.
     *
     * @return A {@link Properties} object containing the loaded configuration.
     * @throws RuntimeException if no configuration file is found.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    static Properties loadConfig() {
        String[] configFiles = {
                "config/application.properties", "config/application.yml",
                "application.properties", "application.yml"
        };

        for (String file : configFiles) {
            Properties properties = getProperties(file);
            if (!properties.isEmpty()) {
                return properties;
            }
        }

        return new Properties();
    }

    /**
     * Loads properties from the given file. Supports both .properties and .yml formats.
     *
     * @param filePath The path of the configuration file.
     * @return A {@link Properties} object containing the parsed configuration.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    private static Properties getProperties(String filePath) {
        Properties properties = new Properties();

        try (InputStream inputStream = getFileStream(filePath)) {
            if (inputStream != null) {
                if (filePath.endsWith(".properties")) {
                    System.out.println("Loading properties file: " + filePath);
                    properties.load(inputStream);
                } else if (filePath.endsWith(".yml")) {
                    System.out.println("Loading YAML file: " + filePath);
                    Yaml yaml = new Yaml();
                    Map<String, Object> yamlMap = yaml.load(inputStream);
                    flattenMap("", yamlMap, properties);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load file (" + filePath + "): " + e.getMessage());
        }

        return properties;
    }

    /**
     * Retrieves an {@link InputStream} for the specified file.
     * <p>
     * The method first attempts to load the file from the classpath.
     * If the file is not found in the classpath, it then checks for an external file
     * in the execution directory.
     * </p>
     * <p>
     * This ensures that classpath resources are prioritized, while allowing
     * external files to override them if present in the execution directory.
     * </p>
     *
     * @param fileName The name of the file to load.
     * @return An {@link InputStream} for the file, or {@code null} if the file is not found.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    private static InputStream getFileStream(String fileName) {
        InputStream classpathStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (classpathStream != null) return classpathStream;

        try {
            return Files.newInputStream(Paths.get(fileName));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Recursively flattens a nested YAML map structure into a properties format.
     * Converts keys like:
     * <pre>
     * log:
     *   level: INFO
     *   filePath: "/var/logs/app.log"
     * </pre>
     * Into:
     * <pre>
     * log.level=INFO
     * log.filePath=/var/logs/app.log
     * </pre>
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