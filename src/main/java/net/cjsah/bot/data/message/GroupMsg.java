package net.cjsah.bot.data.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;
import net.cjsah.bot.data.enums.GroupRole;
import net.cjsah.bot.resolver.JsonEnumMapping;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMsg extends BaseData {
    private int messageId;
    private long groupId;
    private long userId;
    private Anonymous anonymous;
    private Object message;
    private String rawMessage;
    private int font;
    private Sender sender;

    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Sender extends FriendMsg.Sender {
        private String card;
        private String area;
        private String level;
        @JsonEnumMapping
        private GroupRole role;
        private String title;
    }

    @Data
    public static class Anonymous {
        private long id;
        private String name;
        private String flag;
    }
}
