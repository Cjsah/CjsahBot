package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
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

    public AppendRequestEvent(JSONObject json, MessageType type) {
        this.userId = json.getLongValue("user_id");
        this.comment = json.getString("comment");
        this.flag = json.getString("flag");
        this.type = type;
    }

    public static class FriendAppendRequestEvent extends AppendRequestEvent {
        public FriendAppendRequestEvent(JSONObject json) {
            super(json, MessageType.FRIEND);
        }
    }

    @Getter
    public static class GroupAppendRequestEvent extends AppendRequestEvent {
        private final long groupId;
        private final IncreaseType joinType;

        public GroupAppendRequestEvent(JSONObject json, IncreaseType joinType) {
            super(json, MessageType.GROUP);
            this.groupId = json.getLongValue("group_id");
            this.joinType = joinType;
        }

        public static class GroupAppendNormalRequestEvent extends GroupAppendRequestEvent {
            public GroupAppendNormalRequestEvent(JSONObject json) {
                super(json, IncreaseType.APPROVE);
            }
        }

        public static class GroupAppendInviteRequestEvent extends GroupAppendRequestEvent {
            public GroupAppendInviteRequestEvent(JSONObject json) {
                super(json, IncreaseType.INVITE);
            }
        }
    }


}
