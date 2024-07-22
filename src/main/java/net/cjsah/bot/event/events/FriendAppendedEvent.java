package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.event.IEvent;

@Getter
public class FriendAppendedEvent implements IEvent {
    private final long userId;

    public FriendAppendedEvent(JsonNode json) {
        this.userId = json.get("user_id").asLong();
    }
}
