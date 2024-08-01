package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.DecreaseType;
import net.cjsah.bot.event.Event;

@Getter
public class GroupUserLeaveEvent extends Event {
    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final DecreaseType reason;

    public GroupUserLeaveEvent(JSONObject json, DecreaseType reason) {
        this.groupId = json.getLongValue("group_id");
        this.userId = json.getLongValue("user_id");
        this.operatorId = json.getLongValue("operator_id");
        this.reason = reason;
    }

    public static class GroupUserSelfLeaveEvent extends GroupUserLeaveEvent {
        public GroupUserSelfLeaveEvent(JSONObject json) {
            super(json, DecreaseType.LEAVE);
        }
    }

    public static class GroupUserKickEvent extends GroupUserLeaveEvent {
        public GroupUserKickEvent(JSONObject json) {
            super(json, DecreaseType.KICK);
        }
    }

    public static class GroupUserKickMeEvent extends GroupUserLeaveEvent {
        public GroupUserKickMeEvent(JSONObject json) {
            super(json, DecreaseType.KICK_ME);
        }
    }
}
