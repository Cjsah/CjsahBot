package net.cjsah.bot.data.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;
import net.cjsah.bot.data.enums.GroupRole;
import net.cjsah.bot.data.enums.Sex;
import net.cjsah.bot.resolver.JsonEnumMapping;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseMessage extends BaseData {
    private int messageId;
    private long userId;
    private Object message;
    private String rawMessage;
    private int font;
    private Sender sender;

    @Data
    public static class Sender {
        private long userId;
        private String nickname;
        @JsonEnumMapping
        private Sex sex;
        private int age;
        private String card;
        private String area;
        private String level;
        @JsonEnumMapping
        private GroupRole role;
        private String title;
    }

}
