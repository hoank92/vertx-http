package com.hoank.handler;

import com.hoank.model.UserInfo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by hoank92 on Sep, 2019
 */
public interface UserService {
    Future<JsonObject> findOneByUserName(String userName);
    Future<List<UserInfo>> searchByUserName(String userName);
    Future<UserInfo> addUser(UserInfo userInfo);
}
