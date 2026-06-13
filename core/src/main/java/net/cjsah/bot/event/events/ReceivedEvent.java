package net.cjsah.bot.event.events;

import net.cjsah.bot.data.OB11BaseInfo;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.exception.BuiltinExceptions;

public abstract class ReceivedEvent extends Event {
    private OB11BaseInfo base = null;

    public void init(OB11BaseInfo base) {
        if (this.base != null) {
            EventManager.log.warn("The event has already been initialized; there is no need to initialize it again.");
        } else {
            this.base = base;
        }
    }

    public OB11BaseInfo getBase() {
        if (this.base == null) {
            throw BuiltinExceptions.EVENT_NOT_INIT.create();
        }
        return this.base;
    }
}
