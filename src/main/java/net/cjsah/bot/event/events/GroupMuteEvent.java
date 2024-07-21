package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupMuteEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final long duration;
    private final CountStatus type;

    public GroupMuteEvent(JsonNode json, CountStatus type) {
        this.groupId = json.get("group_id").asLong();
        this.userId = json.get("user_id").asLong();
        this.operatorId = json.get("operator_id").asLong();
        this.duration = json.get("duration").asLong();
        this.type = type;
    }

    public static class GroupMuteAppendEvent extends GroupMuteEvent {
        public GroupMuteAppendEvent(JsonNode json) {
            super(json, CountStatus.INCREASE);
        }
    }

    public static class GroupMuteRemoveEvent extends GroupMuteEvent {
        public GroupMuteRemoveEvent(JsonNode json) {
            super(json, CountStatus.DECREASE);
        }
    }
}
