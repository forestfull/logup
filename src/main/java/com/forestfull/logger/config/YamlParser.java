package com.forestfull.logger.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class YamlParser {
	public static Properties parseYaml(InputStream inputStream) throws IOException {
		Properties properties = new Properties();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line; String currentKey = "";

		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}

			if (line.contains(":")) {
				String[] keyValue = line.split(":", 2);
				String key = keyValue[0].trim();
				String value = keyValue.length > 1 ? keyValue[1].trim() : "";
				if (!value.isEmpty()) {
					properties.setProperty(currentKey.isEmpty() ? key : currentKey + "." + key, value);
				} else {
					currentKey = key;
				}
			} else {
				currentKey = "";
			}
		}
		reader.close(); return properties;
	}
}
