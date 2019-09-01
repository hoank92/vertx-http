package com.hoank;

import com.hoank.utils.AppContext;
import com.hoank.verticle.HttpVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.log4j.Log4j2;

/**
 * Created by hoank92 on Aug, 2019
 */

@Log4j2
public class Main {


    public static void main(String[] args) {
        log.info("test");
        AppContext.init();
        initVertx();
    }

    private static void initVertx() {
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setWorkerPoolSize(Integer.parseInt(AppContext.get("worker_size")));
        vertxOptions.setMaxEventLoopExecuteTime(Long.MAX_VALUE);

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(vertxOptions.getEventLoopPoolSize());

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpVerticle.class, deploymentOptions);
    }
}
