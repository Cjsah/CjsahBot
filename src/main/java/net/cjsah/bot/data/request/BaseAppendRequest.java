package net.cjsah.bot.data.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAppendRequest extends BaseData {
    private long userId;
    private String comment;
    private String flag;
}
