package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sex {
    UNKNOWN("unknown"),
    MALE("male"),
    FEMALE("female");

    private final String name;
}
