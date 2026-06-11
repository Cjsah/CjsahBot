package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;

public enum NoticeEventType implements IStrSerializable {
//    GROUP_UPLOAD("group_upload", GroupUploadEvent::new),
//    GROUP_ADMIN("group_admin", GroupAdminChangeEventType::toEvent),
//    GROUP_DECREASE("group_decrease", GroupMemberLeaveEvent::new),
//    GROUP_INCREASE("group_increase", GroupMemberJoinEvent::new),
//    GROUP_BAN("group_ban", GroupMuteEvent::new),
//    FRIEND_ADD("friend_add", FriendAppendedEvent::new),
//    GROUP_RECALL("group_recall", GroupRecallEvent::new),
//    FRIEND_RECALL("friend_recall", FriendRecallEvent::new),
    NOTIFY("notify", NotifyEventType.Builder.CODEC),
    EMPTY("empty", null),
    ;

    public static final Codec<NoticeEventType> CODEC = IStrSerializable.fromEnum(NoticeEventType.class);

    private final String type;
    private final Codec<?> codec;

    NoticeEventType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public record Builder(NoticeEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            NoticeEventType.CODEC.fieldOf("notice_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }

}
