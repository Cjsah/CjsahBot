package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.enums.IncreaseType;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.event.IEvent;

@Getter
public class AppendRequestEvent implements IEvent {
    private final long userId;
    private final String comment;
    private final String flag;
    private final MessageType type;

    public AppendRequestEvent(JsonNode json, MessageType type) {
        this.userId = json.get("user_id").asLong();
        this.comment = json.get("comment").asText();
        this.flag = json.get("flag").asText();
        this.type = type;
    }

    public static class FriendAppendRequestEvent extends AppendRequestEvent {
        public FriendAppendRequestEvent(JsonNode json) {
            super(json, MessageType.FRIEND);
        }
    }

    @Getter
    public static class GroupAppendRequestEvent extends AppendRequestEvent {
        private final long groupId;
        private final IncreaseType joinType;

        public GroupAppendRequestEvent(JsonNode json, IncreaseType joinType) {
            super(json, MessageType.GROUP);
            this.groupId = json.get("group_id").asLong();
            this.joinType = joinType;
        }

        public static class GroupAppendNormalRequestEvent extends GroupAppendRequestEvent {
            public GroupAppendNormalRequestEvent(JsonNode json) {
                super(json, IncreaseType.APPROVE);
            }
        }

        public static class GroupAppendInviteRequestEvent extends GroupAppendRequestEvent {
            public GroupAppendInviteRequestEvent(JsonNode json) {
                super(json, IncreaseType.INVITE);
            }
        }
    }


}
