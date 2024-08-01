package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.event.Event;

@Getter
public class AppConnectedEvent extends Event {
    public AppConnectedEvent(JSONObject json) {}
}
