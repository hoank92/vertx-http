package com.hoank.verticle;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hoank92 on Jan, 2020
 */
public class ReadFileVerticle extends AbstractVerticle {

    private static Logger log = LogManager.getLogger(ReadFileVerticle.class);

    @Override
    public void start(Promise<Void> promise) throws Exception {
        EventBus eb = vertx.eventBus();

        log.info("read file");

        readFile(res -> {
            if (res.failed()) {
                log.error(res.cause());
                promise.fail(res.cause());
            } else {
                List<String> domains = Arrays.asList(res.result().split("\n"));
                int i = 0;
                for (String domain : domains) {
                    i += 1;
                    eb.send("dsn-lookup", domain);
                    if (i % 40 == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                promise.complete();
            }
        });
    }

    // Read file method
    private void readFile(Handler<AsyncResult<String>> resultHandler) {
        // Retrieve a FileSystem object from vertx instance and call the
        // non-blocking readFile method
        vertx.fileSystem().readFile("config/domain.txt", handler -> {
            if (handler.succeeded()) {
                resultHandler.handle(Future.succeededFuture(handler.result().toString()));
            } else {
                log.error(handler.cause());
                resultHandler.handle(Future.failedFuture(handler.cause()));
            }
        });
    }
}
