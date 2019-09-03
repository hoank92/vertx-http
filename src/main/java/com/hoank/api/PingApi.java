package com.hoank.api;

import com.hoank.utils.AppContext;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.Setter;

/**
 * Created by hoank92 on Aug, 2019
 */
@Setter
public class PingApi extends AbstractHttpHandler {
    private KafkaProducer producer;
    public PingApi(Vertx vertx) {
        super(vertx);
    }

    @Override
    public void configRoute(Router router) {
        router.get("/").handler(routingContext -> {
            HttpServerRequest request = routingContext.request();
            JsonObject about = new JsonObject();
            about.put("message", "welcome to vertx service");
            routingContext.response().end(about.encode());
        });

        router.get("/ping").handler(routingContext -> {
            HttpServerRequest request = routingContext.request();
            JsonObject about = new JsonObject();
            about.put("message", "pong");
            KafkaProducerRecord<String, String> record =
                    KafkaProducerRecord.create(AppContext.get("kafka_topic"), null, "ping");
            producer.write(record);
            routingContext.response().end(about.encode());
        });
    }
}
