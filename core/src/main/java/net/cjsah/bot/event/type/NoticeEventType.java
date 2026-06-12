package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.event.events.*;

public enum NoticeEventType implements IStrSerializable {
    FRIEND_ADD("friend_add", FriendAddEvent.CODEC),
    FRIEND_RECALL("friend_recall", FriendRecallEvent.CODEC),
    GROUP_RECALL("group_recall", GroupRecallEvent.CODEC),
    GROUP_INCREASE("group_increase", GroupMemberJoinEvent.CODEC),
    GROUP_DECREASE("group_decrease", GroupMemberLeaveEvent.CODEC),
    GROUP_ADMIN("group_admin", GroupMemberJoinEvent.CODEC),
    GROUP_BAN("group_ban", GroupMuteEvent.CODEC),
    GROUP_UPLOAD("group_upload", GroupUploadEvent.CODEC),
    GROUP_CARD("group_card", GroupCardEvent.CODEC),
    NOTIFY("notify", NotifyEventType.Builder.CODEC),
    ESSENCE("essence", GroupEssenceEvent.CODEC),
    GROUP_EMOJI("group_msg_emoji_like", GroupMessageEmojiLikeEvent.CODEC),
    BOT_OFFLINE("bot_offline", BotOfflineEvent.CODEC),
    ;

    public static final Codec<NoticeEventType> CODEC = IStrSerializable.fromEnum(NoticeEventType.class);

    private final String type;
    private final Codec<?> codec;

    NoticeEventType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
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
