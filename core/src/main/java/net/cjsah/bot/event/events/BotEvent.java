package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

import java.util.Date;

public abstract class BotEvent extends Event {
    protected final Date time;
    protected final long selfId;

    protected BotEvent(JSONObject raw) {
        this.time = new Date(raw.getLongValue("time") * 1000L);
        this.selfId = raw.getLongValue("self_id");
    }

    public Date getTime() {
        return this.time;
    }

    public long getSelfId() {
        return this.selfId;
    }

    @Override
    public String toString() {
        return "BotEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                '}';
    }
}
