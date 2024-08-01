package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.event.Event;

@Getter
public class GroupPokeEvent extends Event {
    private final long groupId;
    private final long userId;
    private final long targetId;

    public GroupPokeEvent(JSONObject json) {
        this.groupId = json.getLongValue("group_id");
        this.userId = json.getLongValue("user_id");
        this.targetId = json.getLongValue("target_id");
    }
}