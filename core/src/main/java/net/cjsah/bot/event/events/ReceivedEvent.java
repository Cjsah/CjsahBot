package net.cjsah.bot.event.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.OB11BaseType;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ReceivedEvent extends Event {
    private final OB11BaseType baseType;
}
