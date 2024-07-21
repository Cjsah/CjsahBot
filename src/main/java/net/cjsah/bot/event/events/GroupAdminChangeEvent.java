package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.IEvent;

@Getter
@RequiredArgsConstructor
public class GroupAdminChangeEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final CountStatus type;

    public static void parse(JsonNode json) {
        long groupId = json.get("group_id").asLong();
        long userId = json.get("user_id").asLong();
        String subType = json.get("sub_type").asText();
        GroupAdminChangeEvent event = null;
        switch (subType) {
            case "set" -> event = new GroupAdminSetEvent(groupId, userId);
            case "unset" -> event = new GroupAdminUnsetEvent(groupId, userId);
        }
        Event.broadcast(event);
    }

    public static class GroupAdminSetEvent extends GroupAdminChangeEvent {
        public GroupAdminSetEvent(long groupId, long userId) {
            super(groupId, userId, CountStatus.INCREASE);
        }
    }

    public static class GroupAdminUnsetEvent extends GroupAdminChangeEvent {
        public GroupAdminUnsetEvent(long groupId, long userId) {
            super(groupId, userId, CountStatus.DECREASE);
        }
    }

}
