package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.api.TypedMessage;
import net.cjsah.bot.event.events.GroupAtMessageEvent;

public final class GroupCommandSource extends CommandSource<GroupAtMessageEvent> {

    public GroupCommandSource(GroupAtMessageEvent sender) {
        super(sender);
    }

    @Override
    public long getSenderId() {
        return this.sender.getSender().getId().hashCode();
    }

    @Override
    public <S> void sendFeedback(TypedMessage<S> message) {
        Api.sendGroupMsg(this.sender.getGroupId(), this.sender.getId(), message);
    }
}
