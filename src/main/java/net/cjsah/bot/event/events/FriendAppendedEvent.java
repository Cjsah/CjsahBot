package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.notice.FriendAdd;
import net.cjsah.bot.event.IEvent;

@Getter
public class FriendAppendedEvent implements IEvent {
    private final long userId;

    public FriendAppendedEvent(FriendAdd data) {
        this.userId = data.getUserId();
    }

    public static void parse(JsonNode json) {

    }
}
