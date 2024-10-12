package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.api.MsgBuilder;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.permission.RoleType;
import net.cjsah.bot.plugin.Plugin;

public record CommandSource(CommandEvent sender) {

    public boolean hasPermission(RoleType role) {
        return PermissionManager.hasPermission("main", this.sender.getRoomId(), this.sender.getChannelId(), this.sender.getSenderId(), role);
    }

    public boolean canUse(Plugin plugin) { //TODO permission abstract
        return true;
    }

    public void sendFeedback(String message) {
        Api.sendMsg(new MsgBuilder(this.sender.getRoomId(), this.sender.getChannelId(), message)
                .at(this.sender.getSenderId())
                .replay(this.sender.getMsgId()));

    }
}
