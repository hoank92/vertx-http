package com.hoank;

import com.hoank.verticle.ConterVerticle;
import com.hoank.verticle.DnsVerticle;
import com.hoank.verticle.ReadFileVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by hoank92 on Jan, 2020
 */
public class DomainLookup {

    private static Logger log = LogManager.getLogger(DomainLookup.class);

    public static void main(String[] args) {
        initVertx();
    }

    private static void initVertx() {

        DeploymentOptions deploymentOptions1 = new DeploymentOptions();
        deploymentOptions1.setInstances(1);

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(8);

        io.vertx.core.Vertx vertx = Vertx.vertx();
//        Promise<String> deploymentFuture = Promise.promise();
//        vertx.deployVerticle(ReadFileVerticle.class, deploymentOptions1, deploymentFuture);
//        log.info("start");
//
//        deploymentFuture.future().compose(id -> {
//            Promise<String> dnsVerticle = Promise.promise();
//            vertx.deployVerticle(
//                    DnsVerticle.class,
//                    deploymentOptions,
//                    dnsVerticle);
//
//            return dnsVerticle.future();
//
//        }).setHandler(ar -> {
//            if (ar.succeeded()) {
//                log.info("start success");
//            } else {
//                log.info("start failed");
//                log.error(ar.cause().getLocalizedMessage());
//            }
//        });
        vertx.deployVerticle(ReadFileVerticle.class, deploymentOptions1);
        vertx.deployVerticle(ConterVerticle.class, deploymentOptions1);

        vertx.deployVerticle(DnsVerticle.class, deploymentOptions);

    }




}
