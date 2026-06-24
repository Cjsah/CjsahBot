package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.util.CodecUtil;

@RequiredArgsConstructor
public enum ImageType {
    IMAGE,
    FACE,
    OTHER,
    ;

    public static final Codec<ImageType> CODEC = CodecUtil.fromEnum(ImageType.class);
}
