package net.cjsah.bot.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FriendData extends UserBaseData {
    private final String remark;

    public FriendData(long userId, String nickname, String remark) {
        super(userId, nickname);
        this.remark = remark;
    }
}
