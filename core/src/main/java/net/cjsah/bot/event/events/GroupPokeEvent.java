package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class GroupPokeEvent extends BotEvent {
    private final long groupId;
    private final long userId;
    private final long pokedId;

    public GroupPokeEvent(JSONObject raw) {
        super(raw);
        this.groupId = raw.getLongValue("group_id");
        this.userId = raw.getLongValue("user_id");
        this.pokedId = raw.getLongValue("target_id");
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getUserId() {
        return this.userId;
    }

    public long getPokedId() {
        return this.pokedId;
    }

    @Override
    public String toString() {
        return "GroupPokeEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", pokeId=" + pokedId +
                '}';
    }
}
