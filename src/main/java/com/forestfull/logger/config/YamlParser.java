package com.forestfull.logger.config;

import java.io.*;
import java.util.*;

public class YamlParser {
    public static Properties parseYaml(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Properties properties = new Properties();
        Map<String, Object> yamlMap = new LinkedHashMap<String, Object>();

        return properties;
    }
}