package com.hoank.api;

import com.hoank.datasource.CacheClient;
import com.hoank.datasource.RedisCacheClient;
import com.hoank.handler.UserHandler;
import com.hoank.handler.UserService;
import com.hoank.model.UserInfo;
import com.hoank.utils.DslJsonUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by hoank92 on Aug, 2019
 */
@Setter
@Log4j2
public class UserApi extends AbstractHttpHandler {
    private CacheClient cacheClient;
    private UserService userService;

    public UserApi(Vertx vertx) {
        super(vertx);
    }

    @Override
    public void configRoute(Router router) {
        router.get("/user").handler(routingContext -> {
            Future<String> future = cacheClient.getValue("hoank");
            future.compose(res -> future).setHandler(res -> {
                if (res.succeeded()) {
                    routingContext.response().end(JsonObject.mapFrom(DslJsonUtils.deserialize(future.result(), UserInfo.class)).encode());
                } else {
                    JsonObject about = new JsonObject();
                    about.put("error", res.result());
                    routingContext.response().end(about.encode());
                }
            });
        });
        router.get("/users").handler(routingContext -> {
            JsonObject about = new JsonObject();
            about.put("users", "hoank");
            routingContext.response().end(about.encode());
        });

        router.get("/user/:id").handler(routingContext -> {
            HttpServerRequest request = routingContext.request();
            String id = request.getParam("id");
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setUsername("hoank");
            userInfo.setFullname("nguyen khanh hoa");
            Future<String> future = cacheClient.insertValue("hoank", DslJsonUtils.serialize(userInfo));
            future.compose(res -> future).setHandler(res -> {
                if (res.succeeded()) {
                    routingContext.response().end(JsonObject.mapFrom(userInfo).encode());
                } else {
                    JsonObject about = new JsonObject();
                    about.put("error", "error");
                    routingContext.response().end(about.encode());
                }
            });
        });

        router.get("/user/username/:username").handler(routingContext -> {
            HttpServerRequest request = routingContext.request();
            String username = request.getParam("username");
            Future<JsonObject> future = userService.findOneByUserName(username);
            future.compose(res -> future).setHandler(res -> {
                if (res.succeeded()) {
                    routingContext.response().end(JsonObject.mapFrom(res.result()).encode());
                } else {
                    JsonObject about = new JsonObject();
                    about.put("error", "error");
                    routingContext.response().end(about.encode());
                }
            });
        });

        router.post("/user").handler(routingContext -> {
           routingContext.request().bodyHandler(res -> {
               UserInfo userInfo = res.toJsonObject().mapTo(UserInfo.class);
               Future<UserInfo> future = userService.addUser(userInfo);
               future.compose(tmp -> future).setHandler(resp -> responseData(resp, routingContext));
           });
        });
    }

    private void responseData(AsyncResult<UserInfo> res, RoutingContext rc) {
        if (res.succeeded()) {
            rc.response().end(JsonObject.mapFrom(res.result()).encode());
        } else {
            JsonObject about = new JsonObject();
            about.put("error", "error");
            rc.response().end(about.encode());
        }
    }
}
