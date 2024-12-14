package net.cjsah.bot;

import net.cjsah.bot.event.CancelableEvent;
import net.cjsah.bot.event.events.AppReconnectEvent;
import net.cjsah.bot.event.events.AppStopEvent;

import java.util.function.Supplier;

public enum SignalType {
    STOP(AppStopEvent::new),
    RESTART, //TODO 未实现
    RE_CONNECT(AppReconnectEvent::new),
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
