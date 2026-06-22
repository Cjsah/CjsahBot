package net.cjsah.bot.event.events;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.util.ExtraCodecs;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupPokeEvent extends PokeEvent {
    public static final Codec<GroupPokeEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(GroupPokeEvent::getUserId),
        Codec.LONG.fieldOf("target_id").forGetter(GroupPokeEvent::getTargetId),
        Codec.LONG.fieldOf("group_id").forGetter(GroupPokeEvent::getGroupId),
        ExtraCodecs.JSON.fieldOf("raw_info").forGetter(GroupPokeEvent::getRawInfo)
    ).apply(instance, GroupPokeEvent::new));

    private final long groupId;
    private final JsonElement rawInfo;

    public GroupPokeEvent(long userId, long targetId, long groupId, JsonElement rawInfo) {
        super(userId, targetId);
        this.groupId = groupId;
        this.rawInfo = rawInfo;
    }
}
