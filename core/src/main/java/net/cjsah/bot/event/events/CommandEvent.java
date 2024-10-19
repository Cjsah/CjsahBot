package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.ChannelInfo;
import net.cjsah.bot.data.CommandInfo;
import net.cjsah.bot.data.RoomInfo;
import net.cjsah.bot.data.UserInfo;
import net.cjsah.bot.event.Event;

public class CommandEvent extends Event {
    private final int botId;
    private final String msgId;
    private final RoomInfo roomInfo;
    private final ChannelInfo channelInfo;
    private final UserInfo senderInfo;
    private final CommandInfo commandInfo;

    public CommandEvent(JSONObject json) {
        this.botId = json.getIntValue("bot_id");
        this.msgId = json.getString("msg_id");

        this.roomInfo = new RoomInfo(json.getJSONObject("room_base_info"));
        this.channelInfo = new ChannelInfo(json.getJSONObject("channel_base_info"));
        this.senderInfo = new UserInfo(json.getJSONObject("sender_info"), this.roomInfo.getId());
        this.commandInfo = new CommandInfo(json.getJSONObject("command_info"));
    }

    public int getBotId() {
        return this.botId;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public RoomInfo getRoomInfo() {
        return this.roomInfo;
    }

    public ChannelInfo getChannelInfo() {
        return this.channelInfo;
    }

    public UserInfo getSenderInfo() {
        return this.senderInfo;
    }

    public CommandInfo getCommandInfo() {
        return this.commandInfo;
    }

    @Override
    public String toString() {
        return "CommandEvent{" +
                "botId=" + botId +
                ", msgId='" + msgId + '\'' +
                ", roomInfo=" + roomInfo +
                ", channelInfo=" + channelInfo +
                ", senderInfo=" + senderInfo +
                ", commandInfo=" + commandInfo +
                '}';
    }
}
