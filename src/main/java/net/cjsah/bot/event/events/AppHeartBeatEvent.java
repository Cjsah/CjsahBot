package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.StatusData;
import net.cjsah.bot.event.IEvent;
import net.cjsah.bot.util.JsonUtil;

@Getter
public class AppHeartBeatEvent implements IEvent {
    private final long interval;
    private final StatusData status;

    public AppHeartBeatEvent(JsonNode json) {
        this.interval = json.get("interval").asLong();
        this.status = JsonUtil.convert(json.get("status"), StatusData.class);
    }

}
