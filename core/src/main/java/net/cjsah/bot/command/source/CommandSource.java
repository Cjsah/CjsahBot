package net.cjsah.bot.command.source;

import net.cjsah.bot.api.TypedMessage;
import net.cjsah.bot.permission.HeyboxPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CommandSource<T> {

    protected final T sender;

    public CommandSource(T sender) {
        this.sender = sender;
    }

    public abstract long getSenderId();

    public boolean hasPermission(HeyboxPermission[] permissions) {
        return true;
//        return PermissionManager.hasPermission(sender.getSenderInfo(), permissions);
    }

    public void sendFeedback(String message) {
        this.sendFeedback(TypedMessage.text(message));
    }

    public abstract <S> void sendFeedback(TypedMessage<S> message);
}
