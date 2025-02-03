package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.CommandInfo;

public class CommandEvent extends Event {

    public CommandEvent(JSONObject json) {
    }

    public int getBotId() {
        return 0;
    }

    public String getMsgId() {
        return null;
    }

    public CommandInfo getCommandInfo() {
        return null;
    }
}
