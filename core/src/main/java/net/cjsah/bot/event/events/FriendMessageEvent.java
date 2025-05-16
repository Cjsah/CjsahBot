package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.FriendCommandSource;

public class FriendMessageEvent extends MessageEvent {
    public FriendMessageEvent(String eventId, JSONObject raw) {
        super(eventId, raw, false);
    }

    @Override
    public CommandSource<?> genCommandSource() {
        return new FriendCommandSource(this);
    }

    @Override
    public String toString() {
        return "FriendMessageEvent{" +
                "super='" + super.toString() + '\'' +
                '}';
    }
}
