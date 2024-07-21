package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.enums.IncreaseType;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.IEvent;

@Getter
@RequiredArgsConstructor
public class GroupUserJoinEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final IncreaseType type;

    public static void parse(JsonNode json) {
        long groupId = json.get("group_id").asLong();
        long userId = json.get("user_id").asLong();
        long operatorId = json.get("operator_id").asLong();
        String subType = json.get("sub_type").asText();
        GroupUserJoinEvent event = null;
        switch (subType) {
            case "approve" -> event = new GroupUserApproveJoinEvent(groupId, userId, operatorId);
            case "invite" -> event = new GroupUserInviteJoinEvent(groupId, userId, operatorId);
        }
        Event.broadcast(event);
    }

    public static class GroupUserApproveJoinEvent extends GroupUserJoinEvent {
        public GroupUserApproveJoinEvent(long groupId, long userId, long operatorId) {
            super(groupId, userId, operatorId, IncreaseType.APPROVE);
        }
    }

    public static class GroupUserInviteJoinEvent extends GroupUserJoinEvent {
        public GroupUserInviteJoinEvent(long groupId, long userId, long operatorId) {
            super(groupId, userId, operatorId, IncreaseType.INVITE);
        }
    }
}
