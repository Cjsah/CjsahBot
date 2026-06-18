package net.cjsah.bot.data;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeTask {
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(
            Thread.ofVirtual().name("time-task-", 0).factory());

    private final Runnable task;
    private final long interval;
    private ScheduledFuture<?> future;
    private boolean started;

    public TimeTask(long seconds, Runnable task) {
        this.interval = seconds;
        this.task = task;
    }

    public synchronized void start() {
        if (started) return;
        started = true;
        this.future = EXECUTOR.scheduleAtFixedRate(task, interval, interval, TimeUnit.SECONDS);
    }

    public synchronized void reset() {
        if (!started) return;
        this.future.cancel(false);
        this.future = EXECUTOR.scheduleAtFixedRate(task, interval, interval, TimeUnit.SECONDS);
    }

    public synchronized void stop() {
        if (!started) return;
        started = false;
        this.future.cancel(false);
    }
}
