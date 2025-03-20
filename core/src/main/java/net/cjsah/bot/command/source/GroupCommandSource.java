package net.cjsah.bot.command.source;

import net.cjsah.bot.event.events.GroupAtMessageEvent;

public final class GroupCommandSource extends CommandSource<GroupAtMessageEvent> {

    public GroupCommandSource(GroupAtMessageEvent sender) {
        super(sender);
    }

    @Override
    public void sendFeedback(String message) {

    }
}
