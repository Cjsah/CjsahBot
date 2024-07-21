package net.cjsah.bot.data.notice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupPoke extends BaseData {
    private long groupId;
    private long userId;
    private long targetId;
}
