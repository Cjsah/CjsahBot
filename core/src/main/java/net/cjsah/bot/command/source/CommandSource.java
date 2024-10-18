package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.api.MsgBuilder;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.permission.RoleType;

public record CommandSource(CommandEvent sender) {

    public boolean hasPermission(String pluginId, RoleType role) {
        return PermissionManager.hasPermission(pluginId, this.sender.getRoomInfo().getId(), this.sender.getChannelInfo().getId(), this.sender.getSenderInfo().getId(), role);
    }

    public void sendFeedback(String message) {
        Api.sendMsg(new MsgBuilder(this.sender.getRoomInfo().getId(), this.sender.getChannelInfo().getId(), message)
                .at(this.sender.getSenderInfo().getId())
                .replay(this.sender.getMsgId()));

    }
}
