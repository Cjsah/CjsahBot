package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupMuteEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final long duration;
    private final CountStatus type;

    public GroupMuteEvent(JSONObject json, CountStatus type) {
        this.groupId = json.getLongValue("group_id");
        this.userId = json.getLongValue("user_id");
        this.operatorId = json.getLongValue("operator_id");
        this.duration = json.getLongValue("duration");
        this.type = type;
    }

    public static class GroupMuteAppendEvent extends GroupMuteEvent {
        public GroupMuteAppendEvent(JSONObject json) {
            super(json, CountStatus.INCREASE);
        }
    }

    public static class GroupMuteRemoveEvent extends GroupMuteEvent {
        public GroupMuteRemoveEvent(JSONObject json) {
            super(json, CountStatus.DECREASE);
        }
    }
}
