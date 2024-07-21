package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupRole {
    MEMBER("member"),
    ADMIN("admin"),
    OWNER("owner");

    private final String name;
}
