package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.RoomInfo;
import net.cjsah.bot.data.UserInfo;
import net.cjsah.bot.data.UserModifyState;
import net.cjsah.bot.event.Event;

public class UserModifyEvent extends Event {
    private final RoomInfo roomInfo;
    private final UserInfo userInfo;
    private final UserModifyState state;

    public UserModifyEvent(JSONObject json, UserModifyState state) {
        this.roomInfo = new RoomInfo(json.getJSONObject("room_base_info"));
        this.userInfo = new UserInfo(json.getJSONObject("sender_info"));
        this.state = state;
    }

    public RoomInfo getRoomInfo() {
        return this.roomInfo;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public UserModifyState getState() {
        return this.state;
    }

    public static UserModifyEvent create(JSONObject json) {
        UserModifyState state = UserModifyState.of(json.getIntValue("state"));
        return switch (state) {
            case JOIN -> new UserJoinEvent(json);
            case LEAVE -> new UserLeaveEvent(json);
        };
    }

    @Override
    public String toString() {
        return "UserModifyEvent{" +
                "roomInfo=" + roomInfo +
                ", senderInfo=" + userInfo +
                ", state=" + state +
                '}';
    }
}
