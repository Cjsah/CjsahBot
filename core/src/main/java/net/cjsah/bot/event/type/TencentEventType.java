package net.cjsah.bot.event.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.events.BotEvent;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.event.events.GroupAtMessageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public enum TencentEventType {
    C2C_MESSAGE_CREATE(FriendMessageEvent::new),
    GROUP_AT_MESSAGE_CREATE(GroupAtMessageEvent::new),

    ;

    private final BiFunction<String, JSONObject, BotEvent> eventFactory;

    TencentEventType(BiFunction<String, JSONObject, BotEvent> eventFactory) {
        this.eventFactory = eventFactory;
        InnerClass.EVENT_MAP.put(this.name(), this);
    }

    public static BotEvent createEvent(JSONObject raw) {
        String eventId = raw.getString("id");
        String type = raw.getString("t");
        JSONObject data = raw.getJSONObject("d");
        TencentEventType eventType = InnerClass.EVENT_MAP.get(type);
        return eventType == null ? null : eventType.eventFactory.apply(eventId, data);
    }

    private static class InnerClass {
        private static final Map<String, TencentEventType> EVENT_MAP = new HashMap<>();
    }
}
