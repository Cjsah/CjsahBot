package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.CountStatus;

public class GroupAdminSetEvent extends GroupAdminChangeEvent {
    public GroupAdminSetEvent(JSONObject raw) {
        super(raw, CountStatus.INCREASE);
    }
}
