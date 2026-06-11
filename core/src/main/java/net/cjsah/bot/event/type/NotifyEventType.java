package net.cjsah.bot.event.type;

import com.alibaba.fastjson2.JSONObject;
import com.mojang.serialization.Codec;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.event.events.Event;
import net.cjsah.bot.event.events.GroupHonorEvent;
import net.cjsah.bot.event.events.GroupLuckyKingEvent;
import net.cjsah.bot.event.events.GroupPokeEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum NotifyEventType implements IStrSerializable {
    POKE("poke", GroupPokeEvent::new),
    LUCKY_KING("lucky_king", GroupLuckyKingEvent::new),
    HONOR("honor", GroupHonorEvent::new),
    ;

    public static final Codec<NotifyEventType> CODEC = IStrSerializable.fromEnum(NotifyEventType.class);

    NotifyEventType(String type, Function<JSONObject, Event> handler) {
        this.type = type;
        this.handler = handler;
    }

    private final String type;
    private final Function<JSONObject, Event> handler;

    public String getType() {
        return this.type;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }
}
