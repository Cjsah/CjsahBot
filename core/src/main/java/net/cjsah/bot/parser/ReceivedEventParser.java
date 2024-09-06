package net.cjsah.bot.parser;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.EventType;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceivedEventParser {

    private static final Logger log = LoggerFactory.getLogger(ReceivedEventParser.class);

    public static void parse(JSONObject raw) {
        int type = raw.getIntValue("type");
        EventType eventType = EventType.getByType(type);
        if (eventType == null) {
            log.warn("Unknown event type: {}, {}", type, raw);
            return;
        }
        JSONObject data = raw.getJSONObject("data");
        Event event = eventType.getFactory().apply(data);
        EventManager.broadcast(event);

    }
}
