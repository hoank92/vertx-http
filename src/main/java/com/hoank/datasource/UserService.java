package com.hoank.datasource;

import com.hoank.datasource.mysql.UserServiceImpl;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

import java.util.HashMap;

/**
 * Created by hoank92 on Jan, 2020
 */

@ProxyGen
@VertxGen
public interface UserService {

    @Fluent
    UserService getAllUser(Handler<AsyncResult<JsonArray>> resultHandler);

    @Fluent
    UserService getUserByUsername(String username, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    UserService createUser(String username, String password, Handler<AsyncResult<Void>> resultHandler);

    @GenIgnore
    static UserService create(JDBCClient dbClient, HashMap<Query, String> sqlQueries, Handler<AsyncResult<UserService>> readyHandler) {
        return new UserServiceImpl(dbClient, sqlQueries, readyHandler);
    }
    @GenIgnore
    static UserService createProxy(Vertx vertx, String address) {
        return new UserServiceVertxEBProxy(vertx, address);
    }
}
