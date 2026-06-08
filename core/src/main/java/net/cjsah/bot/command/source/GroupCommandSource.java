package net.cjsah.bot.command.source;

import net.cjsah.bot.event.events.GroupMessageEvent;

public final class GroupCommandSource extends CommandSource<GroupMessageEvent> {

    public GroupCommandSource(GroupMessageEvent sender) {
        super(sender);
    }

    @Override
    public void sendFeedback(String message) {
    }
}
