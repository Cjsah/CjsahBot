package net.cjsah.bot.data;

import com.mojang.serialization.Codec;
import net.cjsah.bot.event.events.BaseEvent;

public interface IEventBuilder {
    Codec<? extends BaseEvent> codec();
}
