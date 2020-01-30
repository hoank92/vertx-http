package com.hoank.utils;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.redis.client.RedisAPI;

/**
 * Created by hoank92 on Jan, 2020
 */
public class RedisUtilsClient {

    private RedisAPI redis;

    public RedisUtilsClient(RedisAPI redisClient) {
        this.redis = redisClient;
    }



    public Future<Long> getId(String key) {
        Promise<Long> promise = Promise.promise();
        redis.incr(key, res -> {
            if (res.failed()) {
                promise.fail(res.cause());
            } else {
                promise.complete(res.result().toLong());
            }
        });
        return promise.future();
    }

}
