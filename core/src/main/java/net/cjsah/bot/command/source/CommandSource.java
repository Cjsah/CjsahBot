package net.cjsah.bot.command.source;

import com.mojang.datafixers.util.Either;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.config.permission.UserRole;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.packet.PacketHandler;
import net.cjsah.bot.packet.request.payload.RequestPacket;
import net.cjsah.bot.packet.request.payload.SendFriendMsg;
import net.cjsah.bot.packet.response.payload.ResponsePacket;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.Collection;
import java.util.function.Function;

public abstract class CommandSource<T extends CommandSender> {
    @Getter
    protected final T sender;
    protected final PermissionContext permission;

    public CommandSource(T sender) {
        this.sender = sender;
        this.permission = PermissionManager.getInstance().createContext(this.permissionFactory());
    }

    public abstract void sendFeedback(String message);

    public abstract void sendFeedback(MessageChain message);

    protected void sendFeedback(RequestPacket packet) {
        Either<ResponsePacket, String> res = PacketHandler.getInstance().send(packet);
        res.right().ifPresent(msg -> Commands.log.error("Failed to send feed back: {}", msg));
    }

    protected abstract Function<Permissions, PermissionContext> permissionFactory();

    public boolean hasPermission(UserRole role) {
        return this.permission.hasPermission(role);
    }

    public boolean hasPermission(UserRole role, Collection<String> pluginIds) {
        return this.permission.hasPermission(role, pluginIds);
    }
}
