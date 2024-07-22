package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.Anonymous;
import net.cjsah.bot.data.Sender;
import net.cjsah.bot.data.enums.MessageSourceType;
import net.cjsah.bot.data.enums.MessageFrom;
import net.cjsah.bot.event.IEvent;

import java.util.List;

@Getter
public class MessageEvent implements IEvent {
    private final int messageId;
    private final long userId;
    private final List<JSONObject> message;
    private final String rawMessage;
    private final int font;
    private final Sender sender;
    private final MessageFrom messageFrom;
    private final MessageSourceType sourceType;

    public MessageEvent(JSONObject json, MessageFrom messageFrom, MessageSourceType sourceType) {
        this.messageId = json.getIntValue("message_id");
        this.userId = json.getLongValue("user_id");
        this.message = json.getList("message", JSONObject.class);
        this.rawMessage = json.getString("raw_message");
        this.font = json.getIntValue("font");
        this.sender = json.getObject("sender", Sender.class);
        this.messageFrom = messageFrom;
        this.sourceType = sourceType;
    }

    public static class FriendMessageEvent extends MessageEvent {

        public FriendMessageEvent(JSONObject json, MessageSourceType sourceType) {
            super(json, MessageFrom.FRIEND, sourceType);
        }

        public static class FriendNormalMessageEvent extends FriendMessageEvent {
            public FriendNormalMessageEvent(JSONObject json) {
                super(json, MessageSourceType.NORMAL);
            }
        }

        public static class FriendTemporaryMessageEvent extends FriendMessageEvent {
            public FriendTemporaryMessageEvent(JSONObject json) {
                super(json, MessageSourceType.TEMPORARY);
            }
        }

        public static class FriendOtherMessageEvent extends FriendMessageEvent {
            public FriendOtherMessageEvent(JSONObject json) {
                super(json, MessageSourceType.OTHER);
            }
        }
    }

    @Getter
    public static class GroupMessageEvent extends MessageEvent {
        private final long groupId;
        private final Anonymous anonymous;

        public GroupMessageEvent(JSONObject json, MessageSourceType sourceType) {
            super(json, MessageFrom.GROUP, sourceType);
            this.groupId = json.getLongValue("group_id");
            this.anonymous = json.getObject("anonymous", Anonymous.class);
        }

        public static class GroupNormalMessageEvent extends GroupMessageEvent {
            public GroupNormalMessageEvent(JSONObject json) {
                super(json, MessageSourceType.NORMAL);
            }
        }

        public static class GroupAnonymousMessageEvent extends GroupMessageEvent {
            public GroupAnonymousMessageEvent(JSONObject json) {
                super(json, MessageSourceType.ANONYMOUS);
            }
        }

        public static class GroupNoticeMessageEvent extends GroupMessageEvent {
            public GroupNoticeMessageEvent(JSONObject json) {
                super(json, MessageSourceType.NOTICE);
            }
        }

    }
}
