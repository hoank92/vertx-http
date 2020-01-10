package com.hoank;

import com.hoank.utils.AppContext;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hoank92 on Jan, 2020
 */
public class TestAppConfig {

    @Test
    public void test() {
        AppContext.init();

        Long t1 = System.currentTimeMillis();
        int n = 10000000;
        for (int i = 0; i < n; i++) {
            Assert.assertEquals(AppContext.get("api_port"), "8080");
        }

        Long t2 = System.currentTimeMillis();

        System.out.println(t2 - t1);
    }
}
