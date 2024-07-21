package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.notice.GroupLuckyKing;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupRedpackLuckyEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long targetId;

    public GroupRedpackLuckyEvent(GroupLuckyKing data) {
        this.groupId = data.getGroupId();
        this.userId = data.getUserId();
        this.targetId = data.getTargetId();
    }
}
