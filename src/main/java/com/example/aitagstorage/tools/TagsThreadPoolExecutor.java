package com.example.aitagstorage.tools;

import java.util.concurrent.*;

public class TagsThreadPoolExecutor {
    public static ExecutorService fuzzyThreadPoolExecutor(int maxThread, ThreadFactory factory) {
        return new ThreadPoolExecutor(
                1,
                maxThread,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>()
        );
    }
}
