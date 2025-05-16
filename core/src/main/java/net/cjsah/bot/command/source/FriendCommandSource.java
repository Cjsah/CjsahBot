package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.api.TypedMessage;
import net.cjsah.bot.event.events.FriendMessageEvent;

public final class FriendCommandSource extends CommandSource<FriendMessageEvent> {

    public FriendCommandSource(FriendMessageEvent sender) {
        super(sender);
    }

    @Override
    public void sendFeedback(String message) {
        Api.sendFriendMsg(
                this.sender.getSender().getId(),
                this.sender.getId(),
                TypedMessage.text(message)
        );
    }
}
