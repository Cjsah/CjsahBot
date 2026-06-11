package net.cjsah.bot.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.cjsah.bot.event.events.BaseEvent;

public interface IEventBuilder {
    Either<Codec<? extends IEventBuilder>, Codec<? extends BaseEvent>> codec();
}
