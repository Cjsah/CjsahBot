package net.cjsah.bot.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class BaseUserData {
    protected final long userId;
    protected final String nickname;
}
