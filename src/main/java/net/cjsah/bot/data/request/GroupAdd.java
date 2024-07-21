package net.cjsah.bot.data.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupAdd extends BaseData {
    private long groupId;
    private long userId;
    private String comment;
    private String flag;
}
