package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.data.notice.GroupMute;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupMuteEvent implements IEvent {
    private final long userId;
    private final long groupId;
    private final long operateId;
    private final long duration;
    private final CountStatus type;

    public GroupMuteEvent(GroupMute data, CountStatus type) {
        this.groupId = data.getGroupId();
        this.userId = data.getUserId();
        this.operateId = data.getOperateId();
        this.duration = data.getDuration();
        this.type = type;
    }

    public static class GroupMuteAppendEvent extends GroupMuteEvent {
        public GroupMuteAppendEvent(GroupMute data) {
            super(data, CountStatus.INCREASE);
        }
    }

    public static class GroupMuteRemoveEvent extends GroupMuteEvent {
        public GroupMuteRemoveEvent(GroupMute data) {
            super(data, CountStatus.DECREASE);
        }
    }
}
