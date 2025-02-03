package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.api.MsgBuilder;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.permission.HeyboxPermission;
import net.cjsah.bot.permission.PermissionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public record CommandSource(CommandEvent sender) {

    private static final Logger log = LoggerFactory.getLogger("Console");

    public boolean hasPermission(HeyboxPermission[] permissions) {
        return true;
//        return PermissionManager.hasPermission(sender.getSenderInfo(), permissions);
    }

    public void sendFeedback(String message) {
//        long senderId = this.sender.getSenderInfo().getId();
//        this.sendFeedback(message, builder -> builder.atUser(senderId));
    }

    public void sendFeedback(String message, Consumer<MsgBuilder> builderAppender) {
        MsgBuilder builder = new MsgBuilder("roomId", "channelId", message).replay(this.sender.getMsgId());
        builderAppender.accept(builder);
//        log.info("[{}] [{}] [{}({})] <== {}",
//                this.sender.getRoomInfo().getName(),
//                this.sender.getChannelInfo().getName(),
//                this.sender.getSenderInfo().getNickname(),
//                this.sender.getSenderInfo().getId(),
//                message
//        );
        Api.sendMsg(builder, false);
    }
}
