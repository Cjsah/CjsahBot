package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.Anonymous;
import net.cjsah.bot.data.Sender;
import net.cjsah.bot.data.enums.MessageSourceType;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.event.IEvent;
import net.cjsah.bot.util.JsonUtil;

@Getter
public class MessageEvent implements IEvent {
    private final int messageId;
    private final long userId;
    private final Object message;
    private final String rawMessage;
    private final int font;
    private final Sender sender;
    private final MessageType messageType;
    private final MessageSourceType sourceType;

    public MessageEvent(JsonNode json, MessageType messageType, MessageSourceType sourceType) {
        this.messageId = json.get("message_id").asInt();
        this.userId = json.get("user_id").asLong();
        this.message = json.get("message");
        this.rawMessage = json.get("raw_message").asText();
        this.font = json.get("font").asInt();
        this.sender = JsonUtil.convert(json.get("sender"), Sender.class);
        this.messageType = messageType;
        this.sourceType = sourceType;
    }

    public static class FriendMessageEvent extends MessageEvent {

        public FriendMessageEvent(JsonNode json, MessageSourceType sourceType) {
            super(json, MessageType.FRIEND, sourceType);
        }

        public static class FriendNormalMessageEvent extends FriendMessageEvent {
            public FriendNormalMessageEvent(JsonNode json) {
                super(json, MessageSourceType.NORMAL);
            }
        }

        public static class FriendTemporaryMessageEvent extends FriendMessageEvent {
            public FriendTemporaryMessageEvent(JsonNode json) {
                super(json, MessageSourceType.TEMPORARY);
            }
        }

        public static class FriendOtherMessageEvent extends FriendMessageEvent {
            public FriendOtherMessageEvent(JsonNode json) {
                super(json, MessageSourceType.OTHER);
            }
        }
    }

    @Getter
    public static class GroupMessageEvent extends MessageEvent {
        private final long groupId;
        private final Anonymous anonymous;

        public GroupMessageEvent(JsonNode json, MessageSourceType sourceType) {
            super(json, MessageType.GROUP, sourceType);
            this.groupId = json.get("group_id").asLong();
            this.anonymous = JsonUtil.convert(json.get("anonymous"), Anonymous.class);
        }

        public static class GroupNormalMessageEvent extends GroupMessageEvent {
            public GroupNormalMessageEvent(JsonNode json) {
                super(json, MessageSourceType.NORMAL);
            }
        }

        public static class GroupAnonymousMessageEvent extends GroupMessageEvent {
            public GroupAnonymousMessageEvent(JsonNode json) {
                super(json, MessageSourceType.ANONYMOUS);
            }
        }

        public static class GroupNoticeMessageEvent extends GroupMessageEvent {
            public GroupNoticeMessageEvent(JsonNode json) {
                super(json, MessageSourceType.NOTICE);
            }
        }

    }
}
