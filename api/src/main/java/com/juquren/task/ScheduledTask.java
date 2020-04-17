package com.juquren.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTask {
    public void run(){
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        System.out.println(System.currentTimeMillis());
        scheduledThreadPool.scheduleWithFixedDelay(
                () -> System.out.println(System.currentTimeMillis()),5, 3, TimeUnit.SECONDS
        );
    }
}
