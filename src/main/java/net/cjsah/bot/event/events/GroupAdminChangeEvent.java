package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.data.notice.GroupAdminChange;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupAdminChangeEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final CountStatus type;

    public GroupAdminChangeEvent(GroupAdminChange data, CountStatus type) {
        this.groupId = data.getGroupId();
        this.userId = data.getUserId();
        this.type = type;
    }

    public static class GroupAdminIncreaseEvent extends GroupAdminChangeEvent {
        public GroupAdminIncreaseEvent(GroupAdminChange data) {
            super(data, CountStatus.INCREASE);
        }
    }

    public static class GroupAdminDecreaseEvent extends GroupAdminChangeEvent {
        public GroupAdminDecreaseEvent(GroupAdminChange data) {
            super(data, CountStatus.DECREASE);
        }
    }

}
