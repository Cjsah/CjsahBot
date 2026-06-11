package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import net.cjsah.bot.event.type.PostType;

import java.time.Instant;

@Data
public class OB11BaseType {
    public static final Codec<OB11BaseType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("time").forGetter(OB11BaseType::getEpochSeconds),
        Codec.LONG.fieldOf("self_id").forGetter(OB11BaseType::getSelfId),
        PostType.CODEC.fieldOf("post_type").forGetter(OB11BaseType::getPostType)
    ).apply(instance, OB11BaseType::new));

    protected final Instant time;
    protected final long selfId;
    protected final PostType postType;

    public OB11BaseType(long time, long selfId, PostType postType) {
        this.time = Instant.ofEpochSecond(time);
        this.selfId = selfId;
        this.postType = postType;
    }

    public long getEpochSeconds() {
        return this.time.getEpochSecond();
    }
}
