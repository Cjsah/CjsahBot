package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum Sex implements IStrSerializable {
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("unknown"),
    ;

    public static final Codec<Sex> CODEC = IStrSerializable.fromEnum(Sex.class);
    private final String sex;

    @Override
    public String getSerializedName() {
        return this.sex;
    }
}
