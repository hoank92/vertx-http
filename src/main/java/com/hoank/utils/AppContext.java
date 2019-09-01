package com.hoank.utils;

/**
 * Created by hoank92 on Aug, 2019
 */
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Log4j2
public class AppContext {
    private static final String PROP_FILE_NAME = "application.properties";
    private static final String RESOURCE_PATH = "src/main/resources/";

    public static void init() {
        log.info("Load application configurations...");
        String env = System.getProperty("env", "dev");
        String configFile = RESOURCE_PATH + env + "." + PROP_FILE_NAME;
        try {
            loadSystemProperties(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSystemProperties(String filePath) throws IOException {
        InputStream input = new FileInputStream(filePath);
        Properties prop = new Properties();
        prop.load(input);
        Set<Map.Entry<Object, Object>> entries = prop.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            String envVal = System.getenv((String) entry.getKey());
            if (envVal != null) {
                System.setProperty((String) entry.getKey(), envVal);
            } else {
                System.setProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        input.close();
    }

    public static String get(String key) {
        Config config = ConfigFactory.load();
        return config.getString(key);
    }
}