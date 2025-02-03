package net.cjsah.bot.event.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.events.Event;
import net.cjsah.bot.event.events.HeartbeatEvent;
import net.cjsah.bot.event.events.LifecycleEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum NoticeEventType {
    META("lifecycle", LifecycleEvent::new),
    MESSAGE("heartbeat", HeartbeatEvent::new),
    ;

    private static final Logger log = LoggerFactory.getLogger("EventManager");

    NoticeEventType(String type, Function<JSONObject, Event> handler) {
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
        String typeKey = raw.getString("notice_type");
        NoticeEventType type = InnerClass.TYPE_MAP.get(typeKey);
        if (type != null) return type.handler.apply(raw);
        log.warn("Unknown event type: {}, {}", typeKey, raw);
        return null;
    }

    private static class InnerClass {
        private static final Map<String, NoticeEventType> TYPE_MAP = new HashMap<>();
    }
}
