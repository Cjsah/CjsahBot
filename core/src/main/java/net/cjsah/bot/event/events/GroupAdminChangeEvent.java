package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupAdminChangeEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final CountStatus type;

    public GroupAdminChangeEvent(JSONObject json, CountStatus type) {
        this.groupId = json.getLongValue("group_id");
        this.userId = json.getLongValue("user_id");
        this.type = type;
    }

    public static class GroupAdminSetEvent extends GroupAdminChangeEvent {
        public GroupAdminSetEvent(JSONObject json) {
            super(json, CountStatus.INCREASE);
        }
    }

    public static class GroupAdminUnsetEvent extends GroupAdminChangeEvent {
        public GroupAdminUnsetEvent(JSONObject json) {
            super(json, CountStatus.DECREASE);
        }
    }

}
