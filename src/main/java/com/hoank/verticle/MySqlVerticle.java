package com.hoank.verticle;

import com.hoank.datasource.Query;
import com.hoank.datasource.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ServiceBinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by hoank92 on Jan, 2020
 */
public class MySqlVerticle extends AbstractVerticle {

    public static final String CONFIG_USER_JDBC_URL = "jdbc.url";
    public static final String CONFIG_USER_JDBC_DRIVER_CLASS = "jdbc.driver_class";
    public static final String CONFIG_USER_JDBC_MAX_POOL_SIZE = "jdbc.max_pool_size";
    public static final String CONFIG_USER_QUEUE = "userdb.queue";

    @Override
    public void start(Promise<Void> promise) throws Exception {

        HashMap<Query, String> sqlQueries = loadSqlQueries();

        JDBCClient dbClient = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", config().getString(CONFIG_USER_JDBC_URL, "jdbc:hsqldb:file:db/wiki"))
                .put("driver_class", config().getString(CONFIG_USER_JDBC_DRIVER_CLASS, "org.hsqldb.jdbcDriver"))
                .put("max_pool_size", config().getInteger(CONFIG_USER_JDBC_MAX_POOL_SIZE, 30)));

        UserService.create(dbClient, sqlQueries, ready -> {
            if (ready.succeeded()) {
                ServiceBinder binder = new ServiceBinder(vertx);
                binder.setAddress(CONFIG_USER_QUEUE)
                        .register(UserService.class, ready.result()); // <1>
                promise.complete();
            } else {
                promise.fail(ready.cause());
            }
        });
    }

    /*
     * Note: this uses blocking APIs, but data is small...
     */
    private HashMap<Query, String> loadSqlQueries() throws IOException {

        InputStream queriesInputStream = new FileInputStream("config/db-queries.properties");

        Properties queriesProps = new Properties();
        queriesProps.load(queriesInputStream);
        queriesInputStream.close();

        HashMap<Query, String> sqlQueries = new HashMap<>();
        sqlQueries.put(Query.CREATE_TABLE, queriesProps.getProperty("create-user-table"));
        sqlQueries.put(Query.CREATE_USER, queriesProps.getProperty("create-user"));
        sqlQueries.put(Query.GET_ALL_USER, queriesProps.getProperty("all-users"));
        sqlQueries.put(Query.GET_USER, queriesProps.getProperty("get-user"));

        return sqlQueries;
    }
}
