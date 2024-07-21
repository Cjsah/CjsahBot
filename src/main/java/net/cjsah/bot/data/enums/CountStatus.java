package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CountStatus {
    NONE("", ""),
    INCREASE("set", "ban"),
    DECREASE("unset", "lift_ban");

    private final String admin;
    private final String mute;
}
