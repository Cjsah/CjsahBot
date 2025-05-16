package net.cjsah.bot.event.events;

import java.util.concurrent.atomic.AtomicReference;

public abstract class CallbackEvent<T> {
    private final AtomicReference<T> callback = new AtomicReference<>();

    public T getCallback() {
        return this.callback.get();
    }

    public void setCallback(T callback) {
        this.callback.set(callback);
    }
}
