package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.UserData;

public class ConnectionReadyEvent extends Event {
    private final int version;
    private final String sessionId;
    private final UserData user;

    public ConnectionReadyEvent(JSONObject raw) {
        this.version = raw.getIntValue("version");
        this.sessionId = raw.getString("session_id");
        this.user = new UserData(raw.getJSONObject("user"));

    }

    public int getVersion() {
        return this.version;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public UserData getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return "ConnectionReadyEvent{" +
                "version=" + version +
                ", sessionId='" + sessionId + '\'' +
                ", user=" + user +
                '}';
    }
}
