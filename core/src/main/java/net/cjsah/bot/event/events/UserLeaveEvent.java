package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.UserModifyState;

public class UserLeaveEvent extends UserModifyEvent {

    public UserLeaveEvent(JSONObject json) {
        super(json, UserModifyState.LEAVE);
    }

    @Override
    public String toString() {
        return "UserLeaveEvent{} " + super.toString();
    }
}
