package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.Event;

public class ActivityEvent extends Event {
    private final String roomId;
    private final String userId;
    private final int continuousDays;

    public ActivityEvent(JSONObject json) {
        this.roomId = json.getString("room_id");
        this.userId = json.getString("user_id");
        this.continuousDays = json.getIntValue("continuous_days");
    }

    public String getRoomId() {
        return this.roomId;
    }

    public String getUserId() {
        return this.userId;
    }

    public int getContinuousDays() {
        return this.continuousDays;
    }

    @Override
    public String toString() {
        return "ActivityEvent{" +
                "roomId='" + roomId + '\'' +
                ", userId='" + userId + '\'' +
                ", continuousDays=" + continuousDays +
                '}';
    }
}
