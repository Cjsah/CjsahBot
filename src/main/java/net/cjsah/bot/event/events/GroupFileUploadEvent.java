package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.data.notice.GroupFileUpload;
import net.cjsah.bot.event.IEvent;

@Getter
public class GroupFileUploadEvent implements IEvent {
    private final long groupId;
    private final long uploaderId;
    private final GroupFileUpload.InnerFile file;

    public GroupFileUploadEvent(GroupFileUpload data) {
        this.groupId = data.getGroupId();
        this.uploaderId = data.getUserId();
        this.file = data.getFile();
    }
}
