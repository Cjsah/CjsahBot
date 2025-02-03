package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.FileInfo;

public class GroupUploadEvent extends BotEvent {
    private final long groupId;
    private final long uploaderId;
    private final FileInfo file;

    public GroupUploadEvent(JSONObject raw) {
        super(raw);
        this.groupId = raw.getLongValue("group_id");
        this.uploaderId = raw.getLongValue("user_id");
        this.file = new FileInfo(raw.getJSONObject("file"));
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getUploaderId() {
        return this.uploaderId;
    }

    public FileInfo getFile() {
        return this.file;
    }

    @Override
    public String toString() {
        return "GroupUploadEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", groupId=" + groupId +
                ", uploaderId=" + uploaderId +
                ", file=" + file +
                '}';
    }
}
