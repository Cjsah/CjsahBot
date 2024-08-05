package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.data.GroupSourceData;
import net.cjsah.bot.msg.MessageChain;
import org.slf4j.event.Level;

public class GroupCommandSource extends CommandSource<GroupSourceData> {

    public GroupCommandSource(GroupSourceData sender) {
        super(sender);
    }

    @Override
    public void sendFeedback(String message) {
        Api.sendGroupMsg(this.sender.group().getGroupId(), MessageChain.raw(message));
    }

    @Override
    public void sendFeedback(String message, Level level) {
        log.warn("群组消息不应该使用日志形式发送");
        this.sendFeedback(message);
    }
}
