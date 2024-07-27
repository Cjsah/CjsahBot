package net.cjsah.bot.resolver;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPools {
    private static final ExecutorService Executor = new ThreadPoolExecutor(
            5, 10,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

    public static void execute(Runnable runnable) {
        Executor.execute(runnable);
    }

    public static <T> Future<T> submit(Callable<T> runnable) {
        return Executor.submit(runnable);
    }

    public static void shutdown() {
        Executor.shutdown();
    }
}
