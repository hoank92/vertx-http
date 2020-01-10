package com.hoank.verticle;

import com.hoank.api.PingApi;
import com.hoank.api.UserApi;
import com.hoank.datasource.UserService;
import com.hoank.utils.AppContext;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.log4j.Log4j2;

/**
 * Created by hoank92 on Aug, 2019
 */

public class HttpVerticle extends AbstractVerticle {
    private static Logger log = LoggerFactory.getLogger(HttpVerticle.class);

    public static final String CONFIG_USER_QUEUE = "userdb.queue";

    @Override
    public void start(Promise<Void> promise) throws Exception {

        String wikiDbQueue = config().getString(CONFIG_USER_QUEUE, "userdb.queue"); // <1>
        UserService userService = UserService.createProxy(vertx, wikiDbQueue);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        PingApi pingApi = new PingApi(vertx);
        pingApi.configRoute(router);


        UserApi userApi = new UserApi(vertx, userService);
        userApi.configRoute(router);

        final Integer port = 8080;

        final Integer httpPort = config().getInteger("http.port", port);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(httpPort, result -> {
                    if (result.succeeded()) {
                        log.info("Server running, listen port: {}", port);
                        promise.complete();
                    } else {
                        log.error(result.cause());
                        promise.fail(result.cause());
                    }
                });
    }

    @Override
    public void stop() {
        log.info("Shutting down application");
    }
}
