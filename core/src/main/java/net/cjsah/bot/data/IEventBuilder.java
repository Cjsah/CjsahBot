package net.cjsah.bot.data;

import com.mojang.serialization.Codec;

public interface IEventBuilder {
    Codec<?> codec();
}
