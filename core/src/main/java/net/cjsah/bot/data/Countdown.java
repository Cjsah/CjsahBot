package net.cjsah.bot.data;

import java.util.concurrent.atomic.AtomicInteger;

public class Countdown {
    private final int defaultValue;
    private final AtomicInteger value;

    public Countdown(int defaultValue) {
        this.defaultValue = defaultValue;
        this.value = new AtomicInteger(1);
    }

    public void reset() {
        this.value.set(this.defaultValue);
    }

    public boolean tick() {
        if (this.value.get() > 0) {
            return this.value.decrementAndGet() == 0;
        }

        return false;
    }

}
