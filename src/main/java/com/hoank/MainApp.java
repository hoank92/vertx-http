package com.hoank;

import com.hoank.utils.AppContext;
import com.hoank.verticle.HttpVerticle;
import com.hoank.verticle.MySqlVerticle;
import io.vertx.core.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by hoank92 on Aug, 2019
 */

public class MainApp {
    private static Logger log = LoggerFactory.getLogger(MainApp.class);


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
        deploymentOptions.setInstances(2);

        Vertx vertx = Vertx.vertx();
        Promise<String> deploymentFuture = Promise.promise();
        vertx.deployVerticle(MySqlVerticle.class, deploymentOptions, deploymentFuture);

        deploymentFuture.future().compose(id -> {
            log.info(id);
            Promise<String> httpVerticleDeployment = Promise.promise();
            vertx.deployVerticle(
                    HttpVerticle.class,
                    deploymentOptions,
                    httpVerticleDeployment);

            return httpVerticleDeployment.future();

        }).setHandler(ar -> {
            if (ar.succeeded()) {
                log.info("start success");
            } else {
                log.info("start failed");
                log.error(ar.cause().getLocalizedMessage());
            }
        });
    }
}
