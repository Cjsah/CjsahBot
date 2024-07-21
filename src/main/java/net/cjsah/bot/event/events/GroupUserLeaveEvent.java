package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.enums.DecreaseType;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.IEvent;

@Getter
@RequiredArgsConstructor
public class GroupUserLeaveEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final DecreaseType reason;

    public static void parse(JsonNode json) {
        long groupId = json.get("group_id").asLong();
        long userId = json.get("user_id").asLong();
        long operatorId = json.get("operator_id").asLong();
        String subType = json.get("sub_type").asText();
        GroupUserLeaveEvent event = null;
        switch (subType) {
            case "leave" -> event = new GroupUserSelfLeaveEvent(groupId, userId, operatorId);
            case "kick" -> event = new GroupUserKickEvent(groupId, userId, operatorId);
            case "kick_me" -> event = new GroupUserKickMeEvent(groupId, userId, operatorId);
        }
        Event.broadcast(event);
    }

    public static class GroupUserSelfLeaveEvent extends GroupUserLeaveEvent {
        public GroupUserSelfLeaveEvent(long groupId, long userId, long operatorId) {
            super(groupId, userId, operatorId, DecreaseType.LEAVE);
        }
    }

    public static class GroupUserKickEvent extends GroupUserLeaveEvent {
        public GroupUserKickEvent(long groupId, long userId, long operatorId) {
            super(groupId, userId, operatorId, DecreaseType.KICK);
        }
    }

    public static class GroupUserKickMeEvent extends GroupUserLeaveEvent {
        public GroupUserKickMeEvent(long groupId, long userId, long operatorId) {
            super(groupId, userId, operatorId, DecreaseType.KICK_ME);
        }
    }
}
