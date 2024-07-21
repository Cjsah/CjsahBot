package net.cjsah.bot.data.notice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.resolver.JsonEnumMapping;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMute extends BaseData {
    @JsonEnumMapping("mute")
    private CountStatus subType;
    private long userId;
    private long groupId;
    private long operateId;
    private long duration;
}
