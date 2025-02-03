package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.CountStatus;

public class GroupAdminUnsetEvent extends GroupAdminChangeEvent {
    public GroupAdminUnsetEvent(JSONObject raw) {
        super(raw, CountStatus.DECREASE);
    }
}
