package com.forestfull.logger.config;


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
			System.out.println("Loading configuration from " + propertiesFile);
			loadProperties(inputStream);
		} else {
			System.out.println(propertiesFile + " not found, trying " + yamlFile);
			inputStream = classLoader.getResourceAsStream(yamlFile);
			if (inputStream != null) {
				System.out.println("Loading configuration from " + yamlFile);
				loadYaml(inputStream);
			} else {
				System.out.println("No configuration file found in classpath.");
			}
		}
	}

	private void loadProperties(InputStream inputStream) {
		try {
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			System.err.println("Failed to load properties file: " + e.getMessage());
		}
	}

	private void loadYaml(InputStream inputStream) {
		try {
			YamlParser yamlParser = new YamlParser();
			properties.putAll(yamlParser.parseYaml(inputStream));
			inputStream.close();
		} catch (IOException e) {
			System.err.println("Failed to load YAML file: " + e.getMessage());
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
