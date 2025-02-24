package net.cjsah.bot.event.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.ConnectionHelloEvent;
import net.cjsah.bot.event.events.Event;
import net.cjsah.bot.event.events.HeartbeatEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public enum Opcode {
    DISPATCH(0, true, DispatchEventType::toEvent),
    HEARTBEAT(1),
    IDENTIFY(2),
    RESUME(6),
    RECONNECT(7),
    INVALID(9),
    HELLO(10, false, ConnectionHelloEvent::new),
    HEARTBEAT_ACK(11, true, json -> new HeartbeatEvent()),
    HTTP_ACK(12),
    ;

    private static final Logger log = LoggerFactory.getLogger("EventManager");

    Opcode(int code, boolean originRaw, Function<JSONObject, Event> handler) {
        this.code = code;
        this.originRaw = originRaw;
        this.handler = handler;
        InnerClass.TYPE_MAP.put(code, this);
    }

    Opcode(int code) {
        this.code = code;
        this.originRaw = true;
        this.handler = json -> null;
        InnerClass.TYPE_MAP.put(code, this);
    }

    private final int code;
    private final boolean originRaw;
    private final Function<JSONObject, Event> handler;

    public int getCode() {
        return this.code;
    }

    public JSONObject generate(boolean appendNum, Consumer<JSONObject> factory) {
        JSONObject payload = JSONObject.of("op", this.code);
        if (appendNum) {
            int num = EventManager.getEventNum();
            payload.put("s", num == 0 ? null : num);
        }
        if (factory != null) {
            JSONObject data = new JSONObject();
            factory.accept(data);
            payload.put("d", data);
        }
        return payload;
    }

    @Nullable
    public static Event toEvent(JSONObject raw) {
        int code = raw.getIntValue("op");
        Opcode opcode = InnerClass.TYPE_MAP.get(code);
        if (opcode != null) return opcode.handler.apply(opcode.originRaw ? raw : raw.getJSONObject("d"));
        log.warn("Unknown opcode: {}, {}", code, raw);
        return null;
    }

    private static class InnerClass {
        private static final Map<Integer, Opcode> TYPE_MAP = new HashMap<>();
    }

}
