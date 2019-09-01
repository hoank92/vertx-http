package com.hoank.handler;

import com.hoank.model.UserInfo;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

/**
 * Created by hoank92 on Aug, 2019
 */
public class UserHandler implements UserService{
    private MongoClient client;
    private String userCollection = "users";

    public UserHandler(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    @Override
    public Future<JsonObject> findOneByUserName(String userName) {
        JsonObject query = new JsonObject();
        query.put("username", userName);
        Promise<JsonObject> promise = Promise.promise();
        client.findOne(userCollection, query, null, res -> {
            if (res.succeeded()) {
                promise.complete(res.result());
            } else {
                promise.fail(res.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<List<UserInfo>> searchByUserName(String userName) {
        return null;
    }

    @Override
    public Future<UserInfo> addUser(UserInfo userInfo) {
        Promise<UserInfo> promise = Promise.promise();
        client.insert(userCollection, JsonObject.mapFrom(userInfo), res -> {
            if (res.succeeded()) {
                promise.complete(userInfo);
            } else {
                promise.fail(res.cause());
            }
        });
        return promise.future();
    }
}
