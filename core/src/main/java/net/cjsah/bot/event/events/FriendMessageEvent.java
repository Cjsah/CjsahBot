package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.FriendCommandSource;

public class FriendMessageEvent extends MessageEvent {
    public FriendMessageEvent(String eventId, JSONObject raw) {
        super(eventId, raw);
    }

    @Override
    public CommandSource<?> genCommandSource() {
        return new FriendCommandSource(this);
    }
}
