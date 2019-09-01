package com.hoank.utils;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.runtime.Settings;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by hoank92 on Aug, 2019
 */

@Log4j2
public class DslJsonUtils {
    private static final DslJson.Settings SETTINGS = Settings.withRuntime().allowArrayFormat(true).includeServiceLoader();
    private static final DslJson<Object> DSL_JSON = new DslJson<Object>(SETTINGS);


    public static <T> String serialize(T input) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DSL_JSON.serialize(input, os);
            return os.toString(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return "{}";
    }

    private static <T> T deserializeDefault(String jsonData, Class<T> clazz, T defaultValue) {
        try {
            byte[] bytes = jsonData.getBytes(StandardCharsets.UTF_8); //convert string to UTF-8 bytes
            return DSL_JSON.deserialize(clazz, bytes, bytes.length);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return defaultValue;
    }

    public static <T> T deserialize(String jsonData, Class<T> clazz) {
        return deserializeDefault(jsonData, clazz, null);
    }
}