package net.cjsah.bot.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.cjsah.bot.data.enums.GroupRole;
import net.cjsah.bot.data.enums.Sex;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Sender extends UserData {
    private final String card;
    private final String area;
    private final String level;
    private final GroupRole role;
    private final String title;

    public Sender(long userId, String nickname, Sex sex, int age, String card, String area, String level, GroupRole role, String title) {
        super(userId, nickname, sex, age);
        this.card = card;
        this.area = area;
        this.level = level;
        this.role = role;
        this.title = title;
    }
}
