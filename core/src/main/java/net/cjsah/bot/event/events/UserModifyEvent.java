package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.RoomInfo;
import net.cjsah.bot.data.UserInfo;
import net.cjsah.bot.data.UserModifyState;
import net.cjsah.bot.event.Event;

public class UserModifyEvent extends Event {
    private final RoomInfo roomInfo;
    private final UserInfo senderInfo;
    private final UserModifyState state;

    public UserModifyEvent(JSONObject json) {
        this.roomInfo = new RoomInfo(json.getJSONObject("room_base_info"));
        this.senderInfo = new UserInfo(json.getJSONObject("sender_info"));
        this.state = UserModifyState.of(json.getIntValue("state"));
    }

    public RoomInfo getRoomInfo() {
        return this.roomInfo;
    }

    public UserInfo getSenderInfo() {
        return this.senderInfo;
    }

    public UserModifyState getState() {
        return this.state;
    }

    @Override
    public String toString() {
        return "UserModifyEvent{" +
                "roomInfo=" + roomInfo +
                ", senderInfo=" + senderInfo +
                ", state=" + state +
                '}';
    }
}
