package com.hoank.datasource;

import io.vertx.core.Future;

/**
 * Created by hoank92 on Sep, 2019
 */
public interface CacheClient {
    Future<String> getValue(String key);
    Future<String> insertValue(String key, String value);
}
