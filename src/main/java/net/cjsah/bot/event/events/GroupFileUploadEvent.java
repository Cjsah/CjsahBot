package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.InnerFile;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.IEvent;
import net.cjsah.bot.util.JsonUtil;

@Getter
@RequiredArgsConstructor
public class GroupFileUploadEvent implements IEvent {
    private final long groupId;
    private final long uploaderId;
    private final InnerFile file;

    public static void parse(JsonNode json) {
        long groupId = json.get("group_id").asLong();
        long userId = json.get("user_id").asLong();
        InnerFile file = JsonUtil.convert(json.get("file"), InnerFile.class);
        GroupFileUploadEvent event = new GroupFileUploadEvent(groupId, userId, file);
        Event.broadcast(event);
    }
}
