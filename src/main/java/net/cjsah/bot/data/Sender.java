package net.cjsah.bot.data;

import lombok.Data;
import net.cjsah.bot.data.enums.GroupRole;
import net.cjsah.bot.data.enums.Sex;
import net.cjsah.bot.resolver.JsonEnumMapping;

@Data
public class Sender {
    private final long userId;
    private final String nickname;
    @JsonEnumMapping
    private final Sex sex;
    private final int age;
    private final String card;
    private final String area;
    private final String level;
    @JsonEnumMapping
    private final GroupRole role;
    private final String title;
}
