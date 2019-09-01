package com.hoank.datasource;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.redis.RedisClient;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RedisCacheClient {

    private RedisClient client;

    public RedisCacheClient(RedisClient client) {
        this.client = client;
    }

    public Future<String> getValue(String key) {
        Promise<String> promise = Promise.promise();
        client.get(key, res -> {
            if (res.succeeded()) {
                promise.complete(res.result());
            } else {
                promise.fail(res.cause());
            }
        });

        return promise.future();
    }

    public Future<String> insertValue(String key, String value) {
        Promise<String> promise = Promise.promise();
        client.set(key, value, res -> {
            if (res.succeeded()) {
                promise.complete(value);
            } else {
                promise.fail(res.cause());
            }

        });
        return promise.future();
    }

    public RedisClient getClient() {
        return client;
    }

}
