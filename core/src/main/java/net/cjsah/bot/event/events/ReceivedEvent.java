package net.cjsah.bot.event.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.OB11BaseType;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.exception.BuiltExceptions;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ReceivedEvent extends Event {
    private OB11BaseType base = null;

    public void init(OB11BaseType base) {
        if (this.base != null) {
            EventManager.log.warn("The event has already been initialized; there is no need to initialize it again.");
        } else {
            this.base = base;
        }
    }

    public OB11BaseType getBase() {
        if (this.base == null) {
            throw BuiltExceptions.EVENT_NOT_INIT.create();
        }
        return this.base;
    }
}
