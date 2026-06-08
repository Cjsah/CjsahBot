package net.cjsah.bot.command.source;

import net.cjsah.bot.event.events.FriendMessageEvent;

public final class FriendCommandSource extends CommandSource<FriendMessageEvent> {

    public FriendCommandSource(FriendMessageEvent sender) {
        super(sender);
    }

    @Override
    public void sendFeedback(String message) {
    }
}
