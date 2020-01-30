package com.hoank.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.dns.DnsClient;
import io.vertx.core.dns.DnsClientOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * Created by hoank92 on Jan, 2020
 */
public class DnsVerticle extends AbstractVerticle {
    private static Logger log = LogManager.getLogger(DnsVerticle.class);
    private String[] hosts = {"8.8.8.8"};
    private DnsClient dnsClient;



    @Override
    public void start(Promise<Void> promise) throws Exception {
        dnsClient = vertx.createDnsClient(new DnsClientOptions()
//                .setPort(53)
//                .setHost(hosts[new Random().nextInt(hosts.length)])
                .setQueryTimeout(10000)
        );
        //dnsClient = vertx.createDnsClient();
        EventBus eb = vertx.eventBus();
        eb.consumer("dsn-lookup", res -> {
            loopUp(res.body().toString());
        });
    }

    private void loopUp(String domain) {
        EventBus eb = vertx.eventBus();
        dnsClient.lookup4(domain, ar -> {
            if (ar.succeeded()) {
                String record = ar.result();
                eb.send("domain-success",
                        new JsonObject().put("domain", domain)
                                .put("result", record));
            } else {
                //log.error(String.format("Failed to resolve entry%s", ar.cause()));
                eb.send("domain-error",
                        new JsonObject().put("domain", domain)
                                .put("error", ar.cause().getMessage()));
            }
        });
    }
}
