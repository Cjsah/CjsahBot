package net.cjsah.bot.command.source;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.event.events.GroupMessageEvent;
import net.cjsah.bot.permission.context.GroupPermissionContext;
import net.cjsah.bot.permission.context.PermissionContext;

public final class GroupCommandSource extends CommandSource<GroupMessageEvent> {

    public GroupCommandSource(GroupMessageEvent sender) {
        super(sender);
    }

    @Override
    public PermissionContext createPermissionContext(Permissions permissions, String pluginId) {
        return new GroupPermissionContext(permissions, pluginId, this.sender.getUserId(), this.sender.getGroupId());
    }

    @Override
    public void sendFeedback(String message) {
    }
}
