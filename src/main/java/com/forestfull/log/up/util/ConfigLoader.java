package com.forestfull.log.up.util;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * The ConfigLoader class is responsible for loading configuration properties
 * from either an `application.properties` or `application.yml` file.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
public class ConfigLoader {

    /**
     * Loads the configuration properties from the classpath.
     * It first tries to load from `application.properties`, if not found, it attempts to load from `application.yml`.
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
}
