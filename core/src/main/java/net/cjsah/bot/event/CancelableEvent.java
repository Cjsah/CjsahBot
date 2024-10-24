package net.cjsah.bot.event;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class CancelableEvent extends Event {
    private final AtomicBoolean isCancel = new AtomicBoolean(false);

    public boolean isCancel() {
        return this.isCancel.get();
    }

    public synchronized void cancel() {
        this.isCancel.set(true);
    }
}
