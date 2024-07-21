package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.enums.IncreaseType;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupUserJoinEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final IncreaseType type;

    public GroupUserJoinEvent(JsonNode json, IncreaseType type) {
        this.groupId = json.get("group_id").asLong();
        this.userId = json.get("user_id").asLong();
        this.operatorId = json.get("operator_id").asLong();
        this.type = type;
    }

    public static class GroupUserApproveJoinEvent extends GroupUserJoinEvent {
        public GroupUserApproveJoinEvent(JsonNode json) {
            super(json, IncreaseType.APPROVE);
        }
    }

    public static class GroupUserInviteJoinEvent extends GroupUserJoinEvent {
        public GroupUserInviteJoinEvent(JsonNode json) {
            super(json, IncreaseType.INVITE);
        }
    }
}
