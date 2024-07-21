package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.data.request.BaseAppendRequest;
import net.cjsah.bot.data.request.GroupAppendRequest;
import net.cjsah.bot.event.IEvent;

@Getter
public class AppendRequestEvent implements IEvent {
    private final long userId;
    private final String comment;
    private final String flag;
    private final MessageType type;

    public AppendRequestEvent(BaseAppendRequest data, MessageType type) {
        this.userId = data.getUserId();
        this.comment = data.getComment();
        this.flag = data.getFlag();
        this.type = type;
    }

    public static void parse(JsonNode json) {

    }

    public static class FriendAppendRequestEvent extends AppendRequestEvent {
        public FriendAppendRequestEvent(BaseAppendRequest data) {
            super(data, MessageType.FRIEND);
        }
    }

    @Getter
    public static class GroupAppendRequestEvent extends AppendRequestEvent {
        private final long groupId;

        public GroupAppendRequestEvent(GroupAppendRequest data) {
            super(data, MessageType.GROUP);
            this.groupId = data.getGroupId();
        }
    }


}
