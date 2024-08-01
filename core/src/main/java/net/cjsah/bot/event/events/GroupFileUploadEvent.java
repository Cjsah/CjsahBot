package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.InnerFile;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.util.JsonUtil;

@Getter
public class GroupFileUploadEvent extends Event {
    private final long groupId;
    private final long uploaderId;
    private final InnerFile file;

    public GroupFileUploadEvent(JSONObject json) {
        this.groupId = json.getLongValue("group_id");
        this.uploaderId = json.getLongValue("user_id");
        this.file = JsonUtil.getObject(json, "file", InnerFile.class);
    }
}
