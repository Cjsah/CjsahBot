package net.cjsah.bot.data.notice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;
import net.cjsah.bot.data.enums.DecreaseType;
import net.cjsah.bot.resolver.JsonEnumMapping;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupUserIncrease extends BaseData {
    @JsonEnumMapping("type")
    private DecreaseType subType;
    private long userId;
    private long groupId;
    private long operateId;
}
