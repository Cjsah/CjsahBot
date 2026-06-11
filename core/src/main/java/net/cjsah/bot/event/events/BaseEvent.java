package net.cjsah.bot.event.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEvent extends Event {
    private final OB11BaseType baseType;
}
