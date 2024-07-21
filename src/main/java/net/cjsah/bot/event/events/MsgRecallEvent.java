package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.data.notice.GroupMsgRecall;
import net.cjsah.bot.data.notice.MsgRecall;
import net.cjsah.bot.event.IEvent;

@Getter
public class MsgRecallEvent implements IEvent {
    private final long userId;
    private final long messageId;
    private final MessageType type;

    public MsgRecallEvent(MsgRecall data, MessageType type) {
        this.userId = data.getUserId();
        this.messageId = data.getMessageId();
        this.type = type;
    }

    public static class FriendMsgRecallEvent extends MsgRecallEvent {
        public FriendMsgRecallEvent(MsgRecall data) {
            super(data, MessageType.FRIEND);
        }
    }

    @Getter
    public static class GroupMsgRecallEvent extends MsgRecallEvent {
        private final long groupId;
        private final long operatorId;

        public GroupMsgRecallEvent(GroupMsgRecall data) {
            super(data, MessageType.GROUP);
            this.groupId = data.getGroupId();
            this.operatorId = data.getOperatorId();
        }
    }

}
