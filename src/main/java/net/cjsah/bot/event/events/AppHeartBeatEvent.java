package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.StatusData;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.IEvent;
import net.cjsah.bot.util.JsonUtil;

@Getter
@RequiredArgsConstructor
public class AppHeartBeatEvent implements IEvent {
    private final long interval;
    private final StatusData status;

    public static void parse(JsonNode json) {
        long groupId = json.get("interval").asLong();
        StatusData status = JsonUtil.convert(json.get("status"), StatusData.class);
        AppHeartBeatEvent event = new AppHeartBeatEvent(groupId, status);
        Event.broadcast(event);
    }

}
