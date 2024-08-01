package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.event.IEvent;

@Getter
public class FriendAppendedEvent implements IEvent {
    private final long userId;

    public FriendAppendedEvent(JSONObject json) {
        this.userId = json.getLongValue("user_id");
    }
}
