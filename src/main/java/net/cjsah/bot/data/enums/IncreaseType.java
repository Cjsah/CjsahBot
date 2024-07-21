package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IncreaseType {
    NONE(""),
    APPROVE("approve"),
    INVITE("invite");

    private final String type;
}
