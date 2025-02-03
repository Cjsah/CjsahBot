package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.util.EnumUtil;

public class LifecycleEvent extends BotEvent {
    private final Status status;

    public LifecycleEvent(JSONObject raw) {
        super(raw);
        this.status = EnumUtil.ofName(Status.class, raw.getString("sub_type"));
    }

    public Status getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "LifecycleEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", status=" + status +
                '}';
    }

    public enum Status {
        ENABLE,
        DISABLE,
        CONNECT
    }
}
