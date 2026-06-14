package net.cjsah.bot.util;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class LateInit<T> implements Supplier<T> {
    private volatile T value;
    private volatile boolean initialized;

    public static <T> LateInit<T> of() {
        return new LateInit<>();
    }

    public static <T> LateInit<T> of(T value) {
        LateInit<T> li = new LateInit<>();
        li.value = value;
        li.initialized = true;
        return li;
    }

    public boolean set(T value) {
        if (this.initialized) return false;
        synchronized (this) {
            if (this.initialized) return false;
            this.value = value;
            this.initialized = true;
            return true;
        }
    }

    @Override
    public T get() {
        if (!this.initialized) {
            throw new IllegalStateException("LateInit has not been initialized");
        }
        return this.value;
    }

    public T orElse(T fallback) {
        return this.initialized ? this.value : fallback;
    }

    public T orElseGet(Supplier<T> supplier) {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    this.value = supplier.get();
                    this.initialized = true;
                }
            }
        }
        return this.value;
    }

    public T orElseThrow() {
        if (this.initialized) {
            return this.value;
        }

        throw new NoSuchElementException("No value present");
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exception) throws X {
        if (this.initialized) {
            return value;
        }

        throw exception.get();
    }

    public boolean isInitialized() {
        return this.initialized;
    }
}
