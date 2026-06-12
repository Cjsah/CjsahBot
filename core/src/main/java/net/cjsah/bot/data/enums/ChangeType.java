package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum ChangeType {
    INCREASE("add", "set", "ban"),
    DECREASE("delete", "unset", "lift_ban");

    public static final Codec<ChangeType> CODEC_CHANGE = IStrSerializable.fromEnum(ChangeType.class, it -> it.change);
    public static final Codec<ChangeType> CODEC_SET = IStrSerializable.fromEnum(ChangeType.class, it -> it.set);
    public static final Codec<ChangeType> CODEC_MUTE = IStrSerializable.fromEnum(ChangeType.class, it -> it.mute);

    private final String change;
    private final String set;
    private final String mute;
}
