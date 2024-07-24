package net.cjsah.bot.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.cjsah.bot.data.enums.Sex;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserData extends UserBaseData {
    private final Sex sex;
    private final int age;

    public UserData(long userId, String nickname, Sex sex, int age) {
        super(userId, nickname);
        this.sex = sex;
        this.age = age;
    }
}
