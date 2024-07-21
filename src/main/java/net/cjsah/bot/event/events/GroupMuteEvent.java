package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.IEvent;

@Getter
@RequiredArgsConstructor
public class GroupMuteEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final long duration;
    private final CountStatus type;

    public static void parse(JsonNode json) {
        long groupId = json.get("group_id").asLong();
        long userId = json.get("user_id").asLong();
        long operatorId = json.get("operator_id").asLong();
        long duration = json.get("duration").asLong();
        String subType = json.get("sub_type").asText();
        GroupMuteEvent event = null;
        switch (subType) {
            case "ban" -> event = new GroupMuteAppendEvent(groupId, userId, operatorId, duration);
            case "lift_ban" -> event = new GroupMuteRemoveEvent(groupId, userId, operatorId, duration);
        }
        Event.broadcast(event);
    }

    public static class GroupMuteAppendEvent extends GroupMuteEvent {
        public GroupMuteAppendEvent(long groupId, long userId, long operatorId, long duration) {
            super(groupId, userId, operatorId, duration, CountStatus.INCREASE);
        }
    }

    public static class GroupMuteRemoveEvent extends GroupMuteEvent {
        public GroupMuteRemoveEvent(long groupId, long userId, long operatorId, long duration) {
            super(groupId, userId, operatorId, duration, CountStatus.DECREASE);
        }
    }
}
