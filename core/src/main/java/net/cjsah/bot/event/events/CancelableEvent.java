package net.cjsah.bot.event.events;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class CancelableEvent extends Event {
    private final AtomicBoolean canceled = new AtomicBoolean(false);

    public boolean isCanceled() {
        return this.canceled.get();
    }

    public synchronized void cancel() {
        this.canceled.set(true);
    }
}
