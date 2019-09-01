//package com.hoank;
//
//import com.hoank.datasource.RedisCacheClient;
//import io.vertx.core.Future;
//import io.vertx.ext.unit.junit.RunTestOnContext;
//import io.vertx.redis.RedisClient;
//import io.vertx.redis.RedisOptions;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//
///**
// * Created by hoank92 on Aug, 2019
// */
//public class RedisCacheTest {
//
//    @Rule
//    public RunTestOnContext rule = new RunTestOnContext();
//
//    private RedisCacheClient redisCacheClient;
//
//
//    @Before
//    public void setup() {
//        RedisClient client = RedisClient.create(rule.vertx(),
//                new RedisOptions().setHost("localhost"));
//        redisCacheClient = new RedisCacheClient(client);
//    }
//
//    @Test
//    public void testSet() {
//        Future<String> future = redisCacheClient.insertValue("test", "1");
//        if (future.succeeded()) {
//            Assert.assertEquals(future.result(), "1");
//        } else {
//            System.out.println(future.cause().getCause());
//        }
//    }
//
//    @Test
//    public void testSetGet() {
//        Future<String> future = redisCacheClient.insertValue("test", "1");
//        Assert.assertTrue(future.succeeded());
//
//        Future<String> futureRes = redisCacheClient.getValue("test");
//        Assert.assertEquals(futureRes.result(), "1");
//    }
//}