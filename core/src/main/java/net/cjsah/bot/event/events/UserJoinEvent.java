package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.UserModifyState;

public class UserJoinEvent extends UserModifyEvent {

    public UserJoinEvent(JSONObject json) {
        super(json, UserModifyState.JOIN);
    }

    @Override
    public String toString() {
        return "UserJoinEvent{} " + super.toString();
    }
}
