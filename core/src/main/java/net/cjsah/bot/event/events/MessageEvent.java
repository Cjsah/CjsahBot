package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.data.UserData;
import net.cjsah.bot.util.DateUtil;

import java.util.List;

public abstract class MessageEvent extends BotEvent {
    private final UserData sender;
    private final String content;
    private final String id;
    private final long timestamp;
    private final List<JSONObject> attachments;

    public MessageEvent(String eventId, JSONObject raw, boolean isGroup) {
        super(eventId);
        this.sender = new UserData(raw.getJSONObject("author"), isGroup);
        this.content = raw.getString("content");
        this.id = raw.getString("id");
        this.timestamp = DateUtil.parseRFC3339(raw.getString("timestamp"));
        this.attachments = raw.getList("attachments", JSONObject.class);
    }

    public abstract CommandSource<?> genCommandSource();

    public UserData getSender() {
        return this.sender;
    }

    public String getContent() {
        return this.content.trim();
    }

    public String getId() {
        return this.id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public List<JSONObject> getAttachments() {
        return this.attachments;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "sender=" + sender +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", attachments=" + attachments +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
