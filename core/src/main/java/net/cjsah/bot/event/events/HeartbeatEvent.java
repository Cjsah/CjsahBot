package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class HeartbeatEvent extends BotEvent {
    private final long interval;
    private final JSONObject status;

    public HeartbeatEvent(JSONObject raw) {
        super(raw);
        this.interval = raw.getLongValue("interval");
        this.status = raw.getJSONObject("status");
    }

    public long getInterval() {
        return this.interval;
    }

    public JSONObject getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "HeartbeatEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", interval=" + interval +
                ", status=" + status +
                '}';
    }
}
