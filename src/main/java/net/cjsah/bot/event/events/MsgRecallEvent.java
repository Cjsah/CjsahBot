package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.event.IEvent;

@Getter
public class MsgRecallEvent implements IEvent {
    private final long userId;
    private final long messageId;
    private final MessageType type;

    public MsgRecallEvent(JsonNode json, MessageType type) {
        this.userId = json.get("user_id").asLong();
        this.messageId = json.get("message_id").asLong();
        this.type = type;
    }

    public static class FriendMsgRecallEvent extends MsgRecallEvent {
        public FriendMsgRecallEvent(JsonNode json) {
            super(json, MessageType.FRIEND);
        }
    }

    @Getter
    public static class GroupMsgRecallEvent extends MsgRecallEvent {
        private final long groupId;
        private final long operatorId;

        public GroupMsgRecallEvent(JsonNode json) {
            super(json, MessageType.GROUP);
            this.groupId = json.get("group_id").asLong();
            this.operatorId = json.get("operator_id").asLong();
        }
    }

}
