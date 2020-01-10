package com.hoank.datasource.mysql;

import com.hoank.datasource.Query;
import com.hoank.datasource.UserService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hoank92 on Jan, 2020
 */

public class UserServiceImpl implements UserService {

    private final HashMap<Query, String> sqlQueries;
    private final JDBCClient dbClient;
    private static Logger log = LogManager.getLogger(UserServiceImpl.class);


    public UserServiceImpl(JDBCClient dbClient, HashMap<Query, String> sqlQueries, Handler<AsyncResult<UserService>> readyHandler) {
        this.dbClient = dbClient;
        this.sqlQueries = sqlQueries;
        dbClient.getConnection(ar -> {
            if (ar.failed()) {
                log.error("Could not open a database connection ", ar.cause());
                readyHandler.handle(Future.failedFuture(ar.cause()));
            } else {
                SQLConnection connection = ar.result();
                connection.execute(sqlQueries.get(Query.CREATE_TABLE), create -> {
                    connection.close();
                    if (create.failed()) {
                        log.error("Database preparation error", create.cause());
                        readyHandler.handle(Future.failedFuture(create.cause()));
                    } else {
                        readyHandler.handle(Future.succeededFuture(this));
                    }
                });
            }
        });
    }


    @Override
    public UserService getAllUser(Handler<AsyncResult<JsonArray>> resultHandler) {
        this.dbClient.query(sqlQueries.get(Query.GET_ALL_USER), res -> {
            if (res.failed()) {
                log.error("db error ", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            } else {
                JsonArray pages = new JsonArray(new ArrayList<>(res.result().getResults()));
                resultHandler.handle(Future.succeededFuture(pages));
            }
        });
        return this;
    }

    @Override
    public UserService getUserByUsername(String username, Handler<AsyncResult<JsonObject>> resultHandler) {
        dbClient.queryWithParams(sqlQueries.get(Query.GET_USER), new JsonArray().add(username), fetch -> {
            if (fetch.succeeded()) {
                JsonObject response = new JsonObject();
                ResultSet resultSet = fetch.result();
                if (resultSet.getNumRows() == 0) {
                    response.put("found", false);
                } else {
                    response.put("found", true);
                    JsonArray row = resultSet.getResults().get(0);
                    response.put("id", row.getInteger(0));
                    response.put("rawContent", row.getString(1));
                }
                resultHandler.handle(Future.succeededFuture(response));
            } else {
                log.error("Database query error", fetch.cause());
                resultHandler.handle(Future.failedFuture(fetch.cause()));
            }
        });
        return this;
    }

    @Override
    public UserService createUser(String username, String password, Handler<AsyncResult<Void>> resultHandler) {
        System.out.println(username + " " + password);
        JsonArray data = new JsonArray().add(username).add(password);
        dbClient.updateWithParams(sqlQueries.get(Query.CREATE_USER), data, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                log.error("Database query error", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
        return this;
    }
}
