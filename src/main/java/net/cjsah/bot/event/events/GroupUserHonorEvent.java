package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.enums.HonorType;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupUserHonorEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final HonorType type;

    public GroupUserHonorEvent(JsonNode json, HonorType type) {
        this.groupId = json.get("group_id").asLong();
        this.userId = json.get("user_id").asLong();
        this.type = type;
    }

    public static class GroupUserDragonHonorEvent extends GroupUserHonorEvent {
        public GroupUserDragonHonorEvent(JsonNode json) {
            super(json, HonorType.DRAGON);
        }
    }

    public static class GroupUserChatFireHonorEvent extends GroupUserHonorEvent {
        public GroupUserChatFireHonorEvent(JsonNode json) {
            super(json, HonorType.CHAT_FIRE);
        }
    }

    public static class GroupUserHappinessHonorEvent extends GroupUserHonorEvent {
        public GroupUserHappinessHonorEvent(JsonNode json) {
            super(json, HonorType.HAPPINESS);
        }
    }
}
