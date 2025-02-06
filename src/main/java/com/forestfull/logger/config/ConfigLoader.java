package com.forestfull.logger.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    public static Properties loadConfig() {
        String propertiesFile = "application.properties";
        String yamlFile = "application.yml";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(propertiesFile);

        if (inputStream != null) {
            System.out.println("Loading configuration from " + propertiesFile);
            loadProperties(inputStream);
        } else {
            inputStream = classLoader.getResourceAsStream(yamlFile);
            if (inputStream != null) {
                System.out.println("Loading configuration from " + yamlFile);
                loadYaml(inputStream);
            } else {
                System.out.println("No configuration file found in classpath.");
            }
        }
        return properties;
    }

    private static void loadProperties(InputStream inputStream) {
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            System.err.println("Failed to load properties file: " + e.getMessage());
        }
    }

    private static void loadYaml(InputStream inputStream) {
        try {
            properties.putAll(YamlParser.parseYaml(inputStream));
            inputStream.close();
        } catch (IOException e) {
            System.err.println("Failed to load YAML file: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
