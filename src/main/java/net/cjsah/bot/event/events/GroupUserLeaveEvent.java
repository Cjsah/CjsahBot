package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.enums.DecreaseType;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupUserLeaveEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final DecreaseType reason;

    public GroupUserLeaveEvent(JsonNode json, DecreaseType reason) {
        this.groupId = json.get("group_id").asLong();
        this.userId = json.get("user_id").asLong();
        this.operatorId = json.get("operator_id").asLong();
        this.reason = reason;
    }

    public static class GroupUserSelfLeaveEvent extends GroupUserLeaveEvent {
        public GroupUserSelfLeaveEvent(JsonNode json) {
            super(json, DecreaseType.LEAVE);
        }
    }

    public static class GroupUserKickEvent extends GroupUserLeaveEvent {
        public GroupUserKickEvent(JsonNode json) {
            super(json, DecreaseType.KICK);
        }
    }

    public static class GroupUserKickMeEvent extends GroupUserLeaveEvent {
        public GroupUserKickMeEvent(JsonNode json) {
            super(json, DecreaseType.KICK_ME);
        }
    }
}
