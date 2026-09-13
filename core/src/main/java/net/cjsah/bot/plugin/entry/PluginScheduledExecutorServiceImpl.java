package net.cjsah.bot.plugin.entry;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j(topic = "PluginScheduledExecutor")
public class PluginScheduledExecutorServiceImpl implements ScheduledExecutorService {
    private final ScheduledExecutorService delegate;
    private final ScopedValue.Carrier carrier;

    public PluginScheduledExecutorServiceImpl(ScheduledExecutorService delegate, ScopedValue.Carrier carrier) {
        this.delegate = delegate;
        this.carrier = carrier;
    }

    private Runnable wrap(Runnable runnable) {
        return wrap(runnable, false);
    }

    private Runnable wrapPeriodic(Runnable runnable) {
        return wrap(runnable, true);
    }

    private Runnable wrap(Runnable runnable, boolean continueOnException) {
        return () -> this.carrier.run(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.error("Plugin scheduled task execution failed", e);
                if (!continueOnException) {
                    throw e;
                }
            }
        });
    }

    private <T> Callable<T> wrap(Callable<T> callable) {
        return () -> this.carrier.call(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                log.error("Plugin scheduled task execution failed", e);
                throw e;
            }
        });
    }

    @Override
    public @NotNull ScheduledFuture<?> schedule(@NotNull Runnable command, long delay, @NotNull TimeUnit unit) {
        return this.delegate.schedule(wrap(command), delay, unit);
    }

    @Override
    public @NotNull <V> ScheduledFuture<V> schedule(@NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit) {
        return this.delegate.schedule(wrap(callable), delay, unit);
    }

    @Override
    public @NotNull ScheduledFuture<?> scheduleAtFixedRate(@NotNull Runnable command, long initialDelay, long period, @NotNull TimeUnit unit) {
        return this.delegate.scheduleAtFixedRate(wrapPeriodic(command), initialDelay, period, unit);
    }

    @Override
    public @NotNull ScheduledFuture<?> scheduleWithFixedDelay(@NotNull Runnable command, long initialDelay, long delay, @NotNull TimeUnit unit) {
        return this.delegate.scheduleWithFixedDelay(wrapPeriodic(command), initialDelay, delay, unit);
    }

    @Override
    public void shutdown() {
        this.delegate.shutdown();
    }

    @Override
    public @NotNull List<Runnable> shutdownNow() {
        return this.delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return this.delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return this.delegate.awaitTermination(timeout, unit);
    }

    @Override
    public @NotNull <T> Future<T> submit(@NotNull Callable<T> task) {
        return this.delegate.submit(wrap(task));
    }

    @Override
    public @NotNull <T> Future<T> submit(@NotNull Runnable task, T result) {
        return this.delegate.submit(wrap(task), result);
    }

    @Override
    public @NotNull Future<?> submit(@NotNull Runnable task) {
        return this.delegate.submit(wrap(task));
    }

    private <T> Collection<Callable<T>> wrapAll(Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(this::wrap).toList();
    }

    @Override
    public @NotNull <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.delegate.invokeAll(wrapAll(tasks));
    }

    @Override
    public @NotNull <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return this.delegate.invokeAll(wrapAll(tasks), timeout, unit);
    }

    @Override
    public @NotNull <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.delegate.invokeAny(wrapAll(tasks));
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.delegate.invokeAny(wrapAll(tasks), timeout, unit);
    }

    @Override
    public void execute(@NotNull Runnable command) {
        this.delegate.execute(wrap(command));
    }
}
