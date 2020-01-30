package com.hoank;

import com.hoank.utils.RedisUtilsClient;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.redis.RedisClient;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoank92 on Jan, 2020
 */
public class MainRedis {

    private static Logger log = LoggerFactory.getLogger(MainRedis.class);
    public static RedisUtilsClient redisUtilsClient;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Redis client = Redis.createClient(vertx, new RedisOptions());
        client.connect(res1 -> {
            if (res1.failed()) {
                log.error("connect redis error");
                log.error(res1.cause());
            } else {
                log.info("connect redis success");
                redisUtilsClient = new RedisUtilsClient(RedisAPI.api(res1.result()));

                long t1 = System.currentTimeMillis();
                int n = 100;
                List<Future> futureList = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    futureList.add(redisUtilsClient.getId("hoank"));
                }

                CompositeFuture.all(futureList).onComplete(res -> {
                    if (res.failed()) {
                        log.error(res.cause());
                    } else {
                        List<Long> longList = res.result().list();
                        log.info(longList.get(longList.size() - 1));
                    }
                });

                long t2 = System.currentTimeMillis();

                log.info(String.format("ops/s: %d", (t2 - t1) / n));
                log.info(String.format("total time: %d", (t2 - t1)));
            }
        });


    }
}
