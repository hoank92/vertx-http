package com.hoank;

import com.hoank.utils.RedisUtilsClient;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.redis.RedisClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoank92 on Jan, 2020
 */
public class MainRedis {

    private static Logger log = LoggerFactory.getLogger(MainRedis.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        RedisClient client = RedisClient.create(vertx);
        RedisUtilsClient redisUtilsClient = new RedisUtilsClient(client);
        long t1 = System.currentTimeMillis();
        int n = 10000;
        List<Future> listFuture = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            listFuture.add(redisUtilsClient.getId("hoank"));
        }
        CompositeFuture.all(listFuture).onComplete(res -> {
            if (res.failed()) {
                log.error(res.cause());
            } else {
                log.info(res.result());
            }
        }).onFailure(err -> {
            log.error(err);
        });

        long t2 = System.currentTimeMillis();

        log.info(String.format("ops/s: %d", (t2 - t1) / n));
    }
}
