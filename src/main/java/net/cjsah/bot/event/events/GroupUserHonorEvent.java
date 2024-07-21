package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.enums.HonorType;
import net.cjsah.bot.data.notice.GroupUserHonor;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupUserHonorEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final HonorType type;

    public GroupUserHonorEvent(GroupUserHonor data, HonorType type) {
        this.groupId = data.getGroupId();
        this.userId = data.getUserId();
        this.type = type;
    }

    public static class GroupUserDragonHonorEvent extends GroupUserHonorEvent {
        public GroupUserDragonHonorEvent(GroupUserHonor data) {
            super(data, HonorType.DRAGON);
        }
    }

    public static class GroupUserChatFireHonorEvent extends GroupUserHonorEvent {
        public GroupUserChatFireHonorEvent(GroupUserHonor data) {
            super(data, HonorType.CHAT_FIRE);
        }
    }

    public static class GroupUserHappinessHonorEvent extends GroupUserHonorEvent {
        public GroupUserHappinessHonorEvent(GroupUserHonor data) {
            super(data, HonorType.HAPPINESS);
        }
    }
}
