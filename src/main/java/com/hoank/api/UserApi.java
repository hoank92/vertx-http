package com.hoank.api;

import com.hoank.datasource.UserService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by hoank92 on Aug, 2019
 */
public class UserApi extends AbstractHttpHandler {
    private UserService userService;

    private static Logger log = LogManager.getLogger(UserApi.class);


    public UserApi(Vertx vertx, UserService userService) {
        super(vertx);
        this.userService = userService;
    }

    @Override
    public void configRoute(Router router) {
        router.get("/users").handler(this::getAllUser);
        router.get("/users/:username").handler(this::getUserByUsername);
        router.post("/users").handler(this::createUser);

    }

    private void getAllUser(RoutingContext context) {
        userService.getAllUser(res -> {
            if (res.failed()) {
                context.fail(res.cause());
            } else {
                context.response().end(res.result().encode());
            }
        });
    }

    private void getUserByUsername(RoutingContext context) {
        String username = context.request().getParam("username");
        userService.getUserByUsername(username, res -> {
            if (res.failed()) {
                context.fail(res.cause());
            } else {
                context.response().end(res.result().encode());
            }
        });
    }

    private void createUser(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        if (ObjectUtils.isEmpty(body)) {
            context.response()
                    .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end(new JsonObject().put("msg", "body is null").encode());
            return;
        }
        String username = body.getString("username");
        String password= body.getString("password");
        userService.createUser(username, password, res -> {
            if (res.failed()) {
                context.fail(res.cause());
            } else {
                context.response().end(new JsonObject().put("msg", "success").encode());
            }
        });
    }
}
