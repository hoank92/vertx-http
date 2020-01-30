package com.hoank.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by hoank92 on Jan, 2020
 */
public class ConterVerticle extends AbstractVerticle {
    private static Logger log = LogManager.getLogger(ConterVerticle.class);
    private EventBus eb;


    @Override
    public void start(Promise<Void> promise) throws Exception {
        AtomicInteger totalSuccess = new AtomicInteger();
        AtomicInteger totalError = new AtomicInteger();
        eb = vertx.eventBus();
        AtomicLong t = new AtomicLong(System.currentTimeMillis());

        eb.consumer("domain-success", res -> {
            totalSuccess.addAndGet(1);
            AtomicLong t1 = new AtomicLong(System.currentTimeMillis());
            log.info(String.format("total domain error %d", totalError.get()));
            log.info(String.format("total domain success %d", totalSuccess.get()));
            log.info(String.format("total time %d", t1.get() - t.get()));
        });

        eb.consumer("domain-error", res -> {
            totalError.addAndGet(1);
            AtomicLong t2 = new AtomicLong(System.currentTimeMillis());
            log.info(String.format("total domain error %d", totalError.get()));
            log.info(String.format("total domain success %d", totalSuccess.get()));
            log.info(String.format("total time %d", t2.get() - t.get()));
        });

    }
}
