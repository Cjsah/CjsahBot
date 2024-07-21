package net.cjsah.bot.data.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMessage extends BaseMessage {
    private long groupId;
    private Anonymous anonymous;

    @Data
    public static class Anonymous {
        private long id;
        private String name;
        private String flag;
    }
}
