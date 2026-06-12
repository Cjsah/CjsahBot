package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

import java.time.Instant;

@Deprecated
public abstract class BotEvent extends Event {
    protected final Instant time;
    protected final long selfId;

    protected BotEvent(JSONObject raw) {
        this.time = Instant.ofEpochSecond(raw.getLongValue("time"));
        this.selfId = raw.getLongValue("self_id");
    }

    protected BotEvent(long time, long selfId) {
        this.time = Instant.ofEpochSecond(time);
        this.selfId = selfId;
    }

    public Instant getTime() {
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
