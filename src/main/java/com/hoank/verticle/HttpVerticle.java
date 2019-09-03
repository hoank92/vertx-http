package com.hoank.verticle;

import com.hoank.api.PingApi;
import com.hoank.api.UserApi;
import com.hoank.datasource.CacheClient;
import com.hoank.datasource.KafkaClient;
import com.hoank.datasource.RedisCacheClient;
import com.hoank.handler.UserHandler;
import com.hoank.handler.UserService;
import com.hoank.utils.AppContext;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import lombok.extern.log4j.Log4j2;

/**
 * Created by hoank92 on Aug, 2019
 */

@Log4j2
public class HttpVerticle extends AbstractVerticle {
    @Override
    public void start(Future<Void> future) throws Exception {

        //Inject dependency
        log.info("Starting Inject Dependency for verticle {}", Thread.currentThread().getName());
        RedisClient client = RedisClient.create(vertx,
                new RedisOptions().setHost(AppContext.get("redis_host")));
        MongoClient mongoClient = MongoClient.createShared(vertx, getMongoConfig());

        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, KafkaClient.getConfigKafkaProducer());


        Router router = Router.router(vertx);
        PingApi pingApi = new PingApi(vertx);
        pingApi.setProducer(producer);
        pingApi.configRoute(router);


        CacheClient redisCacheClient = new RedisCacheClient(client);
        UserService userService = new UserHandler(mongoClient);

        UserApi userApi = new UserApi(vertx);
        userApi.setCacheClient(redisCacheClient);
        userApi.setUserService(userService);
        userApi.configRoute(router);

        final Integer httpPort = config().getInteger("http.port", 8080);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(httpPort, result -> {
                    if (result.succeeded()) {
                        log.info("Server running, listen port: 8080");
                    } else {
                        log.error(result.cause());
                    }
                });
    }

    private JsonObject getMongoConfig() {
        String uri = AppContext.get("mongo_uri");
        if (uri == null) {
            uri = "mongodb://localhost:27017";
        }
        String db = AppContext.get("mongo_db");
        if (db == null) {
            db = "test";
        }

        JsonObject mongoconfig = new JsonObject()
                .put("connection_string", uri)
                .put("db_name", db);
        return mongoconfig;
    }

    @Override
    public void stop() {
        log.info("Shutting down application");
    }
}
