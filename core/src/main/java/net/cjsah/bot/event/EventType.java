package net.cjsah.bot.event;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.event.events.MessageEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum EventType {
    @Deprecated
    MESSAGE(5, MessageEvent::new),
    COMMAND(50, CommandEvent::new)
    ;

    EventType(int type, Function<JSONObject, Event> factory) {
        this.type = type;
        this.factory = factory;
    }

    private final int type;
    private final Function<JSONObject, Event> factory;

    public int getType() {
        return this.type;
    }

    public Function<JSONObject, Event> getFactory() {
        return this.factory;
    }

    @Nullable
    public static EventType getByType(int type) {
        for (EventType eventType : values()) {
            if (eventType.type == type) return eventType;
        }
        return null;
    }
}
