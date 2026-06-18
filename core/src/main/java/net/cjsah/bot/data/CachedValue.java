package net.cjsah.bot.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class CachedValue<T> {
    private static final ExecutorService EXECUTOR = Executors.newThreadPerTaskExecutor(
            Thread.ofVirtual().name("cached-value-", 0).factory());

    private final Supplier<T> fetcher;
    private final long timeout;
    private volatile T value;
    private volatile long lastUpdateTime;
    private volatile boolean refreshing;

    public CachedValue(Supplier<T> fetcher, long timeoutSeconds) {
        this.fetcher = fetcher;
        this.timeout = timeoutSeconds * 1000;
    }

    public T get() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    value = fetcher.get();
                    lastUpdateTime = System.currentTimeMillis();
                }
            }
            return value;
        }

        if (System.currentTimeMillis() - lastUpdateTime >= timeout) {
            refreshAsync();
        }
        return value;
    }

    private void refreshAsync() {
        synchronized (this) {
            if (refreshing) return;
            refreshing = true;
        }
        EXECUTOR.execute(() -> {
            try {
                T newValue = fetcher.get();
                synchronized (CachedValue.this) {
                    value = newValue;
                    lastUpdateTime = System.currentTimeMillis();
                }
            } finally {
                refreshing = false;
            }
        });
    }
}
