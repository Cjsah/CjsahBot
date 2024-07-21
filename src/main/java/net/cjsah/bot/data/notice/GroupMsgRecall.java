package net.cjsah.bot.data.notice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMsgRecall extends MsgRecall {
    private long groupId;
    private long operatorId;
}
