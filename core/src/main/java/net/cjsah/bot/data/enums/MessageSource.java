package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum MessageSource implements IStrSerializable {
    FRIEND("private"),
    GROUP("group");

    public static final Codec<MessageSource> CODEC = IStrSerializable.fromEnum(MessageSource.class);
    private final String type;

    @Override
    public String getSerializedName() {
        return this.type;
    }
}
