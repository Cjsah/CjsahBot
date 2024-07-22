package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.event.IEvent;

@Getter
public class AppConnectedEvent implements IEvent {
    public AppConnectedEvent(JSONObject json) {}
}
