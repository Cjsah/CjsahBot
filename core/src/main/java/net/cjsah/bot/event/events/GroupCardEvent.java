package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupCardEvent extends ReceivedEvent {
    public static final Codec<GroupCardEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupCardEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupCardEvent::getUserId),
        Codec.STRING.fieldOf("card_old").forGetter(GroupCardEvent::getCardOld),
        Codec.STRING.fieldOf("card_new").forGetter(GroupCardEvent::getCardNew)
    ).apply(instance, GroupCardEvent::new));

    private final long groupId;
    private final long userId;
    private final String cardOld;
    private final String cardNew;
}
