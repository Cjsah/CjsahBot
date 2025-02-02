package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class LifecycleEvent extends BotEvent {
    private final Status status;

    public LifecycleEvent(JSONObject raw) {
        super(raw);
        String subType = raw.getString("sub_type");
        this.status = Status.valueOf(subType.toUpperCase());
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
