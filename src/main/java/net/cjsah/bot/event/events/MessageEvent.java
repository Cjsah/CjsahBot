package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.enums.MessageSourceType;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.data.message.BaseMessage;
import net.cjsah.bot.data.message.GroupMessage;
import net.cjsah.bot.event.IEvent;

@Getter
public class MessageEvent implements IEvent {
    private final int messageId;
    private final long userId;
    private final Object message;
    private final String rawMessage;
    private final int font;
    private final BaseMessage.Sender sender;
    private final MessageType messageType;

    public MessageEvent(BaseMessage data, MessageType messageType) {
        this.messageId = data.getMessageId();
        this.userId = data.getUserId();
        this.message = data.getMessage();
        this.rawMessage = data.getRawMessage();
        this.font = data.getFont();
        this.sender = data.getSender();
        this.messageType = messageType;
    }

    @Getter
    public static class FriendMessageEvent extends MessageEvent {
        private final MessageSourceType sourceType;

        public FriendMessageEvent(BaseMessage data, MessageSourceType sourceType) {
            super(data, MessageType.FRIEND);
            this.sourceType = sourceType;
        }

        public static class FriendNormalMessageEvent extends FriendMessageEvent {
            public FriendNormalMessageEvent(BaseMessage data) {
                super(data, MessageSourceType.NORMAL);
            }
        }

        public static class FriendTemporaryMessageEvent extends FriendMessageEvent {
            public FriendTemporaryMessageEvent(BaseMessage data) {
                super(data, MessageSourceType.TEMPORARY);
            }
        }

        public static class FriendOtherMessageEvent extends FriendMessageEvent {
            public FriendOtherMessageEvent(BaseMessage data) {
                super(data, MessageSourceType.OTHER);
            }
        }
    }

    @Getter
    public static class GroupMessageEvent extends MessageEvent {
        private final long groupId;
        private final GroupMessage.Anonymous anonymous;
        private final MessageSourceType sourceType;

        public GroupMessageEvent(GroupMessage data, MessageSourceType sourceType) {
            super(data, MessageType.GROUP);
            this.groupId = data.getGroupId();
            this.anonymous = data.getAnonymous();
            this.sourceType = sourceType;
        }

        public static class GroupNormalMessageEvent extends GroupMessageEvent {
            public GroupNormalMessageEvent(GroupMessage data) {
                super(data, MessageSourceType.NORMAL);
            }
        }

        public static class GroupAnonymousMessageEvent extends GroupMessageEvent {
            public GroupAnonymousMessageEvent(GroupMessage data) {
                super(data, MessageSourceType.ANONYMOUS);
            }
        }

        public static class GroupNoticeMessageEvent extends GroupMessageEvent {
            public GroupNoticeMessageEvent(GroupMessage data) {
                super(data, MessageSourceType.NOTICE);
            }
        }

    }
}
