package net.cjsah.bot.data;

import net.cjsah.bot.MainApplication;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedValue<T> {
    private static final ExecutorService EXECUTOR = Executors.newThreadPerTaskExecutor(
            Thread.ofVirtual().name("cached-value-", 0).factory());

    private final Fetcher<T> fetcher;
    private final long timeout;
    private volatile T value;
    private volatile long lastUpdateTime;
    private volatile boolean refreshing;
    private String error;

    public CachedValue(Fetcher<T> fetcher, long timeoutSeconds) {
        this.fetcher = fetcher;
        this.timeout = timeoutSeconds * 1000;
    }

    public T get() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    try {
                        value = fetcher.fetch();
                        lastUpdateTime = System.currentTimeMillis();
                    } catch (Exception e) {
                        MainApplication.log.warn("Failed to fetch value", e);
                        error = e.getMessage();
                    }
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
                T newValue = fetcher.fetch();
                synchronized (CachedValue.this) {
                    value = newValue;
                    lastUpdateTime = System.currentTimeMillis();
                }
            } catch (Exception e) {
                error = e.getMessage();
            } finally {
                refreshing = false;
            }
        });
    }

    public Optional<String> getError() {
        return Optional.ofNullable(this.error);
    }

    public interface Fetcher<T> {
        T fetch() throws Exception;
    }
}
