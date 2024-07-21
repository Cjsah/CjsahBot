package net.cjsah.bot.data.notice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMute extends BaseData {
    private long userId;
    private long groupId;
    private long operateId;
    private long duration;
}
