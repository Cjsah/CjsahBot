package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.enums.DecreaseType;
import net.cjsah.bot.data.notice.GroupUserDecrease;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupUserLeaveEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operateId;
    private final DecreaseType reason;

    public GroupUserLeaveEvent(GroupUserDecrease data, DecreaseType reason) {
        this.groupId = data.getGroupId();
        this.userId = data.getUserId();
        this.operateId = data.getOperateId();
        this.reason = reason;
    }
    
    public static class GroupUserSelfLeaveEvent extends GroupUserLeaveEvent {
        public GroupUserSelfLeaveEvent(GroupUserDecrease data) {
            super(data, DecreaseType.LEAVE);
        }
    }

    public static class GroupUserKickEvent extends GroupUserLeaveEvent {
        public GroupUserKickEvent(GroupUserDecrease data) {
            super(data, DecreaseType.KICK);
        }
    }

    public static class GroupUserKickMeEvent extends GroupUserLeaveEvent {
        public GroupUserKickMeEvent(GroupUserDecrease data) {
            super(data, DecreaseType.KICK_ME);
        }
    }
}
