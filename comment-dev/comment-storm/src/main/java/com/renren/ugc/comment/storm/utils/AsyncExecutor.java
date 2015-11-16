package com.renren.ugc.comment.storm.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncExecutor {

    private static AsyncExecutor instance = new AsyncExecutor();

    private Executor executor = Executors.newCachedThreadPool();

    public static AsyncExecutor getInstance() {
        return instance;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
