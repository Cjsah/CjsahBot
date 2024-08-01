package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.Anonymous;
import net.cjsah.bot.data.Sender;
import net.cjsah.bot.data.enums.MessageSendType;
import net.cjsah.bot.data.enums.MessageSource;
import net.cjsah.bot.event.IEvent;
import net.cjsah.bot.msg.MessageChain;
import net.cjsah.bot.util.JsonUtil;
import net.cjsah.bot.util.StringUtil;

@Getter
public class MessageEvent implements IEvent {
    private final int messageId;
    private final long userId;
    private final MessageChain message;
    private final String rawMessage;
    private final int font;
    private final Sender sender;
    private final MessageSource messageSource;
    private final MessageSendType sendType;

    public MessageEvent(JSONObject json, MessageSource messageSource, MessageSendType sendType) {
        this.messageId = json.getIntValue("message_id");
        this.userId = json.getLongValue("user_id");
        this.message = JsonUtil.getObject(json, "message", MessageChain.class);
        this.rawMessage = StringUtil.netReplace(json.getString("raw_message"));
        this.font = json.getIntValue("font");
        this.sender = JsonUtil.getObject(json, "sender", Sender.class);
        this.messageSource = messageSource;
        this.sendType = sendType;
    }

    public static class FriendMessageEvent extends MessageEvent {

        public FriendMessageEvent(JSONObject json, MessageSendType sendType) {
            super(json, MessageSource.FRIEND, sendType);
        }

        public static class FriendNormalMessageEvent extends FriendMessageEvent {
            public FriendNormalMessageEvent(JSONObject json) {
                super(json, MessageSendType.NORMAL);
            }
        }

        public static class FriendTemporaryMessageEvent extends FriendMessageEvent {
            public FriendTemporaryMessageEvent(JSONObject json) {
                super(json, MessageSendType.TEMPORARY);
            }
        }

        public static class FriendOtherMessageEvent extends FriendMessageEvent {
            public FriendOtherMessageEvent(JSONObject json) {
                super(json, MessageSendType.OTHER);
            }
        }
    }

    @Getter
    public static class GroupMessageEvent extends MessageEvent {
        private final long groupId;
        private final Anonymous anonymous;

        public GroupMessageEvent(JSONObject json, MessageSendType sendType) {
            super(json, MessageSource.GROUP, sendType);
            this.groupId = json.getLongValue("group_id");
            this.anonymous = JsonUtil.getObject(json, "anonymous", Anonymous.class);
        }

        public static class GroupNormalMessageEvent extends GroupMessageEvent {
            public GroupNormalMessageEvent(JSONObject json) {
                super(json, MessageSendType.NORMAL);
            }
        }

        public static class GroupAnonymousMessageEvent extends GroupMessageEvent {
            public GroupAnonymousMessageEvent(JSONObject json) {
                super(json, MessageSendType.ANONYMOUS);
            }
        }

        public static class GroupNoticeMessageEvent extends GroupMessageEvent {
            public GroupNoticeMessageEvent(JSONObject json) {
                super(json, MessageSendType.NOTICE);
            }
        }

    }
}
