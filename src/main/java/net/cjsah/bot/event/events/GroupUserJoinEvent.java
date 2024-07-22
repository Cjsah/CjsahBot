package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.IncreaseType;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupUserJoinEvent implements IEvent {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final IncreaseType type;

    public GroupUserJoinEvent(JSONObject json, IncreaseType type) {
        this.groupId = json.getLongValue("group_id");
        this.userId = json.getLongValue("user_id");
        this.operatorId = json.getLongValue("operator_id");
        this.type = type;
    }

    public static class GroupUserApproveJoinEvent extends GroupUserJoinEvent {
        public GroupUserApproveJoinEvent(JSONObject json) {
            super(json, IncreaseType.APPROVE);
        }
    }

    public static class GroupUserInviteJoinEvent extends GroupUserJoinEvent {
        public GroupUserInviteJoinEvent(JSONObject json) {
            super(json, IncreaseType.INVITE);
        }
    }
}
