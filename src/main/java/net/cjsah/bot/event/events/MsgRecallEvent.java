package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.event.IEvent;

@Getter
public class MsgRecallEvent implements IEvent {
    private final long userId;
    private final long messageId;
    private final MessageType type;

    public MsgRecallEvent(JSONObject json, MessageType type) {
        this.userId = json.getLongValue("user_id");
        this.messageId = json.getLongValue("message_id");
        this.type = type;
    }

    public static class FriendMsgRecallEvent extends MsgRecallEvent {
        public FriendMsgRecallEvent(JSONObject json) {
            super(json, MessageType.FRIEND);
        }
    }

    @Getter
    public static class GroupMsgRecallEvent extends MsgRecallEvent {
        private final long groupId;
        private final long operatorId;

        public GroupMsgRecallEvent(JSONObject json) {
            super(json, MessageType.GROUP);
            this.groupId = json.getLongValue("group_id");
            this.operatorId = json.getLongValue("operator_id");
        }
    }

}
