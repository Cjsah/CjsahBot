package net.cjsah.bot.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBaseData {
    private final long userId;
    private final String nickname;
}
