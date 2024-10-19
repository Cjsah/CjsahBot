package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.api.MsgBuilder;
import net.cjsah.bot.data.RoleInfo;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.permission.Permission;

public record CommandSource(CommandEvent sender) {

    public boolean hasPermission(Permission[] permissions) {
        role:
        for (RoleInfo role : sender.getSenderInfo().getRoles()) {
            for (Permission permission : permissions) {
                if ((role.getPermissions() & permission.getValue()) == 0) {
                    continue role;
                }
            }
            return true;
        }
        return false;
    }

    public void sendFeedback(String message) {
        Api.sendMsg(new MsgBuilder(this.sender.getRoomInfo().getId(), this.sender.getChannelInfo().getId(), message)
                .at(this.sender.getSenderInfo().getId())
                .replay(this.sender.getMsgId()));

    }
}
