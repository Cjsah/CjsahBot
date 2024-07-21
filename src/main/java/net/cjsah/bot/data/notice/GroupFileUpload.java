package net.cjsah.bot.data.notice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;
import net.cjsah.bot.data.vo.InnerFile;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupFileUpload extends BaseData {
    private long groupId;
    private long userId;
    private InnerFile file;
}
