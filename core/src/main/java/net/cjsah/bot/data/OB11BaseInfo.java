package net.cjsah.bot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import net.cjsah.bot.event.type.PostType;

import java.time.Instant;

@Data
public class OB11BaseInfo {
    public static final MapCodec<OB11BaseInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.LONG.fieldOf("time").forGetter(OB11BaseInfo::getEpochSeconds),
        Codec.LONG.fieldOf("self_id").forGetter(OB11BaseInfo::getSelfId),
        PostType.CODEC.fieldOf("post_type").forGetter(OB11BaseInfo::getPostType)
    ).apply(instance, OB11BaseInfo::new));

    public static final Codec<OB11BaseInfo> CODEC = MAP_CODEC.codec();

    protected final Instant time;
    protected final long selfId;
    protected final PostType postType;

    public OB11BaseInfo(long time, long selfId, PostType postType) {
        this.time = Instant.ofEpochSecond(time);
        this.selfId = selfId;
        this.postType = postType;
    }

    public long getEpochSeconds() {
        return this.time.getEpochSecond();
    }
}
