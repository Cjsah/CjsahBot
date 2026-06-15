package net.cjsah.bot.command.source;

import com.mojang.datafixers.util.Either;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.event.events.GroupMessageEvent;
import net.cjsah.bot.packet.PacketHandler;
import net.cjsah.bot.packet.request.payload.RequestPacket;
import net.cjsah.bot.packet.request.payload.SendFriendMsg;
import net.cjsah.bot.packet.request.payload.SendGroupMsg;
import net.cjsah.bot.packet.response.payload.ResponsePacket;
import net.cjsah.bot.permission.context.FriendPermissionContext;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.function.Function;

public final class FriendCommandSource extends CommandSource<FriendMessageEvent> {

    public FriendCommandSource(FriendMessageEvent sender) {
        super(sender);
    }

    @Override
    protected Function<Permissions, PermissionContext> permissionFactory() {
        return permissions -> new FriendPermissionContext(permissions, this.sender.getUserId());
    }

    @Override
    public void sendFeedback(String message) {
        FriendMessageEvent sender = this.sender;
        RequestPacket packet = new SendFriendMsg(sender.getUserId(), message);
        Either<ResponsePacket, String> res = PacketHandler.getInstance().send(packet);
        res.right().ifPresent(msg -> Commands.log.error("Failed to send feed back: {}", msg));
    }
}
