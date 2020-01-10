package com.hoank.utils;

/**
 * Created by hoank92 on Aug, 2019
 */

import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class AppContext {
    private static Logger log = LogManager.getLogger(AppContext.class);

    public static void init() {
        log.info("Load application configurations...");
        var configFile = System.getProperty("user.dir") + "/config/local.properties";
        try {
            loadSystemProperties(configFile);
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public static void loadSystemProperties(String filePath) throws IOException {
        var input = new FileInputStream(filePath);
        var prop = new Properties();
        prop.load(input);
        var entries = prop.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            var envVal = System.getenv((String) entry.getKey());
            if (envVal != null) {
                System.setProperty((String) entry.getKey(), envVal);
            } else {
                System.setProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        input.close();
    }

    public static String get(String key) {
        var config = ConfigFactory.load();
        return config.getString(key);
    }
}