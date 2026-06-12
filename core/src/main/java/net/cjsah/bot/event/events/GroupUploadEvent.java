package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.FileInfo;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupUploadEvent extends ReceivedEvent {
    public static final Codec<GroupUploadEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupUploadEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupUploadEvent::getUploaderId),
        FileInfo.CODEC.fieldOf("file").forGetter(GroupUploadEvent::getFile)
    ).apply(instance, GroupUploadEvent::new));

    private final long groupId;
    private final long uploaderId;
    private final FileInfo file;

    public GroupUploadEvent(long groupId, long uploaderId, FileInfo file) {
        this.groupId = groupId;
        this.uploaderId = uploaderId;
        this.file = file;
    }
}
