package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum LifecycleStatus implements IStrSerializable {
    ENABLE("enable"),
    DISABLE("disable"),
    CONNECT("connect"),
    ;

    public static final Codec<LifecycleStatus> CODEC = IStrSerializable.fromEnum(LifecycleStatus.class);
    private final String text;

    @Override
    public String getSerializedName() {
        return this.text;
    }
}
