package com.hoank.datasource;

import com.hoank.utils.AppContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoank92 on Sep, 2019
 */
public class KafkaClient {
    public static Map<String, String> getConfigKafkaProducer() {
        // Prepare kafka configurations
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", AppContext.get("bootstrap_servers"));
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");

        return config;
    }

    public static Map<String, String> getConfigKafkaConsumer(String group) {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", AppContext.get("bootstrap_servers"));
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", group);
        config.put("kafka.auto.offset.reset", "latest");
        config.put("kafka.enable.auto.commit", "false");
        config.put("acks", "1");

        return config;
    }
}
