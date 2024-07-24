package net.cjsah.bot.data;

import lombok.Data;
import net.cjsah.bot.data.enums.GroupRole;
import net.cjsah.bot.data.enums.Sex;

@Data
public class GroupUserData {
    private final long groupId;
    private final long userId;
    private final String nickName;
    private final String card;
    private final Sex sex;
    private final int age;
    private final String area;
    private final int joinTime;
    private final int lastSentTime;
    private final String level;
    private final GroupRole role;
    private final boolean unfriendly;
    private final String title;
    private final int titleExpireTime;
    private final boolean cardChangeable;
}
