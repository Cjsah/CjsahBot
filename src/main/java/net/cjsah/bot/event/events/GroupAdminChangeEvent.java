package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupAdminChangeEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final CountStatus type;

    public GroupAdminChangeEvent(JsonNode json, CountStatus type) {
        this.groupId = json.get("group_id").asLong();
        this.userId = json.get("user_id").asLong();
        this.type = type;
    }

    public static class GroupAdminSetEvent extends GroupAdminChangeEvent {
        public GroupAdminSetEvent(JsonNode json) {
            super(json, CountStatus.INCREASE);
        }
    }

    public static class GroupAdminUnsetEvent extends GroupAdminChangeEvent {
        public GroupAdminUnsetEvent(JsonNode json) {
            super(json, CountStatus.DECREASE);
        }
    }

}
