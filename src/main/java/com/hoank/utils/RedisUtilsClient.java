package com.hoank.utils;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.redis.RedisClient;

/**
 * Created by hoank92 on Jan, 2020
 */
public class RedisUtilsClient {

    private RedisClient client;

    public RedisUtilsClient(RedisClient redisClient) {
        this.client = redisClient;
    }

    public Future<Long> getId(String key) {
        Promise<Long> promise = Promise.promise();
        client.incr(key, res -> {
            if (res.failed()) {
                promise.fail(res.cause());
            } else {
                promise.complete(res.result());
            }
        });
        return promise.future();
    }

}
