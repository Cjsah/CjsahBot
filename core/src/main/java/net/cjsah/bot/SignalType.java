package net.cjsah.bot;

import net.cjsah.bot.event.events.CancelableEvent;
import net.cjsah.bot.event.events.AppStopEvent;

import java.util.function.Supplier;

public enum SignalType {
    STOP(AppStopEvent::new),
    RESTART,
    ;

    private final Supplier<CancelableEvent> event;

    SignalType() {
        this(() -> null);
    }

    SignalType(Supplier<CancelableEvent> event) {
        this.event = event;
    }

    public Supplier<CancelableEvent> getEvent() {
        return this.event;
    }
}
