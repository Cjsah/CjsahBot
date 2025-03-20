package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.FriendCommandSource;

public class FriendMessageEvent extends MessageEvent {
    public FriendMessageEvent(JSONObject raw) {
        super(raw);
    }

    @Override
    public CommandSource<?> genCommandSource() {
        return new FriendCommandSource(this);
    }
}
