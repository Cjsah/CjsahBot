package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.api.TypedMessage;
import net.cjsah.bot.event.events.FriendMessageEvent;

public final class FriendCommandSource extends CommandSource<FriendMessageEvent> {

    public FriendCommandSource(FriendMessageEvent sender) {
        super(sender);
    }

    @Override
    public long getSenderId() {
        return this.sender.getSender().getId().hashCode();
    }

    @Override
    public <S> void sendFeedback(TypedMessage<S> message) {
        Api.sendFriendMsg(
                this.sender.getSender().getId(),
                this.sender.getId(),
                message
        );
    }
}
