package com.forestfull.logger.config;


import com.forestfull.logger.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties = new Properties();

    public ConfigLoader() {
        loadConfig();
    }

    private void loadConfig() {
        String propertiesFile = "application.properties";
        String yamlFile = "application.yml";

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(propertiesFile);

        if (inputStream != null) {
            Log.info("Loading configuration from ", propertiesFile);
            loadProperties(inputStream);
        } else {
            inputStream = classLoader.getResourceAsStream(yamlFile);
            if (inputStream != null) {
                Log.info("Loading configuration from ", yamlFile);
                loadYaml(inputStream);
            } else {
                Log.info("No configuration file found in classpath.");
            }
        }
    }

    private void loadProperties(InputStream inputStream) {
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            Log.error("Failed to load properties file: " + e.getMessage());
        }
    }

    private void loadYaml(InputStream inputStream) {
        try {
            YamlParser yamlParser = new YamlParser();
            properties.putAll(yamlParser.parseYaml(inputStream));
            inputStream.close();
        } catch (IOException e) {
            Log.error("Failed to load YAML file: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
