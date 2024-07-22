package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.StatusData;
import net.cjsah.bot.event.IEvent;

@Getter
public class AppHeartBeatEvent implements IEvent {
    private final long interval;
    private final StatusData status;

    public AppHeartBeatEvent(JSONObject json) {
        this.interval = json.getLongValue("interval");
        this.status = json.getObject("status", StatusData.class);
    }

}
