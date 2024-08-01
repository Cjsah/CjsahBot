package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.HonorType;
import net.cjsah.bot.event.Event;

@Getter
public class GroupUserHonorEvent extends Event {
    private final long groupId;
    private final long userId;
    private final HonorType type;

    public GroupUserHonorEvent(JSONObject json, HonorType type) {
        this.groupId = json.getLongValue("group_id");
        this.userId = json.getLongValue("user_id");
        this.type = type;
    }

    public static class GroupUserDragonHonorEvent extends GroupUserHonorEvent {
        public GroupUserDragonHonorEvent(JSONObject json) {
            super(json, HonorType.DRAGON);
        }
    }

    public static class GroupUserChatFireHonorEvent extends GroupUserHonorEvent {
        public GroupUserChatFireHonorEvent(JSONObject json) {
            super(json, HonorType.CHAT_FIRE);
        }
    }

    public static class GroupUserHappinessHonorEvent extends GroupUserHonorEvent {
        public GroupUserHappinessHonorEvent(JSONObject json) {
            super(json, HonorType.HAPPINESS);
        }
    }
}
