package com.rmatushkin.util;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newWorkStealingPool;

public class ThreadUtil {

    public static ExecutorService createExecutorService(int threadsQuantity) {
        if (threadsQuantity == 0) {
            return newWorkStealingPool();
        } else {
            return newFixedThreadPool(threadsQuantity);
        }
    }
}
