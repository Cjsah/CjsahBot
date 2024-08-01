package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupRedpackLuckyEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long targetId;

    public GroupRedpackLuckyEvent(JSONObject json) {
        this.groupId = json.getLongValue("group_id");
        this.userId = json.getLongValue("user_id");
        this.targetId = json.getLongValue("target_id");
    }
}
