package net.cjsah.bot.command.source;

import com.mojang.datafixers.util.Either;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.event.events.GroupMessageEvent;
import net.cjsah.bot.packet.PacketHandler;
import net.cjsah.bot.packet.request.payload.RequestPacket;
import net.cjsah.bot.packet.request.payload.SendGroupMsg;
import net.cjsah.bot.packet.response.payload.ResponsePacket;
import net.cjsah.bot.permission.context.GroupPermissionContext;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.function.Function;

public final class GroupCommandSource extends CommandSource<GroupMessageEvent> {

    public GroupCommandSource(GroupMessageEvent sender) {
        super(sender);
    }

    @Override
    protected Function<Permissions, PermissionContext> permissionFactory() {
        return permissions -> new GroupPermissionContext(permissions, this.sender.getUserId(), this.sender.getGroupId());
    }

    @Override
    public void sendFeedback(String message) {
        GroupMessageEvent sender = this.sender;
        RequestPacket packet = new SendGroupMsg(sender.getGroupId(), message);
        Either<ResponsePacket, String> res = PacketHandler.getInstance().send(packet);
        res.right().ifPresent(msg -> Commands.log.error("Failed to send feed back: {}", msg));
    }
}
