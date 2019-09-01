package com.hoank.api;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import lombok.Data;

/**
 * Created by hoank92 on May, 2019
 */

@Data
public abstract class AbstractHttpHandler {
    protected Vertx vertx;

    public AbstractHttpHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    public abstract void configRoute(Router router);

}
