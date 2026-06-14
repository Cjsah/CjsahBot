package net.cjsah.bot.event.events;

import net.cjsah.bot.data.OB11BaseInfo;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.util.LateInit;

public abstract class ReceivedEvent extends Event {
    private final LateInit<OB11BaseInfo> base = LateInit.of();
    private final LateInit<Long> websocketId = LateInit.of();

    public void init(long id, OB11BaseInfo base) {
        boolean success = this.websocketId.set(id);
        success = this.base.set(base) && success;

        if (!success) {
            EventManager.log.warn("The event has already been initialized; there is no need to initialize it again.");
        }
    }

    public OB11BaseInfo getBase() {
        return this.base.orElseThrow(BuiltinExceptions.EVENT_NOT_INIT::create);
    }

    public long getWebSocketId() {
        return this.websocketId.orElseThrow(BuiltinExceptions.EVENT_NOT_INIT::create);
    }
}
