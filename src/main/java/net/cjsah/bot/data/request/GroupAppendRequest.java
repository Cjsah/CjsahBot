package net.cjsah.bot.data.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupAppendRequest extends BaseAppendRequest {
    private long groupId;
}
