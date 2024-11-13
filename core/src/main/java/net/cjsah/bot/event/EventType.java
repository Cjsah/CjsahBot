package net.cjsah.bot.event;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.events.CardButtonClickEvent;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.event.events.MessageEmojiEvent;
import net.cjsah.bot.event.events.MessageEvent;
import net.cjsah.bot.event.events.UserModifyEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum EventType {
    @Deprecated
    MESSAGE("5", MessageEvent::new),
    COMMAND("50", CommandEvent::new),
    USER_MODIFY("3001", UserModifyEvent::create),
    MSG_EMOJI("5003", MessageEmojiEvent::create),
    CARD_BUTTON("card_message_btn_click", CardButtonClickEvent::new),
    ;

    EventType(String type, Function<JSONObject, Event> factory) {
        this.type = type;
        this.factory = factory;
        InnerClass.EVENT_TYPE_MAP.put(type, this);
    }

    private final String type;
    private final Function<JSONObject, Event> factory;

    public String getType() {
        return this.type;
    }

    public Function<JSONObject, Event> getFactory() {
        return this.factory;
    }

    @Nullable
    public static EventType getByType(String type) {
        return InnerClass.EVENT_TYPE_MAP.get(type);
    }

    private static class InnerClass {
        private static final Map<String, EventType> EVENT_TYPE_MAP = new HashMap<>();
    }
}
