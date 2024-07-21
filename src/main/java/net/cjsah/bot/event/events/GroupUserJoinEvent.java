package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.enums.IncreaseType;
import net.cjsah.bot.data.notice.GroupUserIncrease;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupUserJoinEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operateId;
    private final IncreaseType type;

    public GroupUserJoinEvent(GroupUserIncrease data, IncreaseType type) {
        this.groupId = data.getGroupId();
        this.userId = data.getUserId();
        this.operateId = data.getOperateId();
        this.type = type;
    }

    public static class GroupUserApproveJoinEvent extends GroupUserJoinEvent {
        public GroupUserApproveJoinEvent(GroupUserIncrease data) {
            super(data, IncreaseType.APPROVE);
        }
    }

    public static class GroupUserInviteJoinEvent extends GroupUserJoinEvent {
        public GroupUserInviteJoinEvent(GroupUserIncrease data) {
            super(data, IncreaseType.INVITE);
        }
    }
}
