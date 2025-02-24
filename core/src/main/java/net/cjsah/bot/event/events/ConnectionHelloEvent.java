package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class ConnectionHelloEvent extends Event {
    private final long heartbeatInterval;

    public ConnectionHelloEvent(JSONObject raw) {
        this.heartbeatInterval = raw.getLongValue("heartbeat_interval");
    }

    public long getHeartbeatInterval() {
        return this.heartbeatInterval;
    }

    @Override
    public String toString() {
        return "ConnectionHelloEvent{" +
                "heartbeatInterval=" + heartbeatInterval +
                '}';
    }
}
