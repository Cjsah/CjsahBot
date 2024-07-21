package net.cjsah.bot.event.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.cjsah.bot.data.InnerFile;
import net.cjsah.bot.event.IEvent;
import net.cjsah.bot.util.JsonUtil;

@Getter
public class GroupFileUploadEvent implements IEvent {
    private final long groupId;
    private final long uploaderId;
    private final InnerFile file;

    public GroupFileUploadEvent(JsonNode json) {
        this.groupId = json.get("group_id").asLong();
        this.uploaderId = json.get("user_id").asLong();
        this.file = JsonUtil.convert(json.get("file"), InnerFile.class);
    }
}
