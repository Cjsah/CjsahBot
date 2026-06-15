package net.cjsah.bot.packet.response;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum ResponseStatus implements IStrSerializable {
    OK("ok"),
    FAILED("failed"),
    ASYNC("async"),
    ;

    public static final Codec<ResponseStatus> CODEC = IStrSerializable.fromEnum(ResponseStatus.class);
    private final String status;

    @Override
    public String getSerializedName() {
        return this.status;
    }
}
