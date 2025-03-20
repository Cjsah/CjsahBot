package net.cjsah.bot.event.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.events.ConnectionReadyEvent;
import net.cjsah.bot.event.events.Event;
import net.cjsah.bot.event.events.GroupAtMessageEvent;
import net.cjsah.bot.event.events.FriendMessageEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum DispatchEventType {
    READY("READY", ConnectionReadyEvent::new),
    GROUP_AT_MSG("GROUP_AT_MESSAGE_CREATE", GroupAtMessageEvent::new),
    FRIEND_MSG("C2C_MESSAGE_CREATE", FriendMessageEvent::new),
    ;

    private static final Logger log = LoggerFactory.getLogger("EventManager");

    DispatchEventType(String type, Function<JSONObject, Event> handler) {
        this.type = type;
        this.handler = handler;
        InnerClass.TYPE_MAP.put(type, this);
    }

    private final String type;
    private final Function<JSONObject, Event> handler;

    public String getType() {
        return this.type;
    }

    @Nullable
    public static Event toEvent(JSONObject raw) {
        String typeKey = raw.getString("t");
        DispatchEventType type = InnerClass.TYPE_MAP.get(typeKey);
        if (type != null) return type.handler.apply(raw.getJSONObject("d"));
        log.warn("Unknown dispatch event: {}, {}", typeKey, raw);
        return null;
    }

    private static class InnerClass {
        private static final Map<String, DispatchEventType> TYPE_MAP = new HashMap<>();
    }

}
