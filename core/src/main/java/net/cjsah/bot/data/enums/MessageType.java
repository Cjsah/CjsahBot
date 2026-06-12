package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum MessageType implements IStrSerializable {
    FRIEND("private"),
    GROUP("group");

    public static final Codec<MessageType> CODEC = IStrSerializable.fromEnum(MessageType.class);
    private final String type;

    @Override
    public String getSerializedName() {
        return this.type;
    }
}
