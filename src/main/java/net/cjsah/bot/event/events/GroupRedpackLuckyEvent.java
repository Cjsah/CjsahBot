package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupRedpackLuckyEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long targetId;

    public GroupRedpackLuckyEvent(JsonNode json) {
        this.groupId = json.get("group_id").asLong();
        this.userId = json.get("user_id").asLong();
        this.targetId = json.get("target_id").asLong();
    }
}
