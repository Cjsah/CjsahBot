package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSourceContent;
import net.cjsah.bot.event.Event;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandEvent extends Event implements CommandSourceContent {
    private final int botId;
    private final String msgId;

    private final String roomAvatar;
    private final String roomId;
    private final String roomName;

    private final String channelId;
    private final String channelName;
    private final int channelType;

    private final String senderAvatar;
    private final int senderId;
    private final String senderName;
    private final int senderLevel;

    private final String cmdId;
    private final String cmdName;
    private final List<CommandNode> cmdOptions;

    public CommandEvent(JSONObject json) {
        this.botId = json.getIntValue("bot_id");
        this.msgId = json.getString("msg_id");
        JSONObject room = json.getJSONObject("room_base_info");
        this.roomAvatar = room.getString("room_avatar");
        this.roomId = json.getString("room_id");
        this.roomName = json.getString("room_name");
        JSONObject channel = json.getJSONObject("channel_base_info");
        this.channelId = channel.getString("channel_id");
        this.channelName = channel.getString("channel_name");
        this.channelType = channel.getIntValue("channel_type");
        JSONObject sender = json.getJSONObject("sender_info");
        this.senderAvatar = sender.getString("avatar");
        this.senderId = sender.getIntValue("user_id");
        this.senderName = sender.getString("nickname");
        this.senderLevel = sender.getIntValue("level");
        JSONObject command = json.getJSONObject("command_info");
        this.cmdId = command.getString("id");
        this.cmdName = command.getString("name");
        List<CommandNode> options = command.getList("options", CommandNode.class);
        this.cmdOptions = options == null ? Collections.emptyList() : options;
    }

    public int getBotId() {
        return this.botId;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public String getRoomAvatar() {
        return this.roomAvatar;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public int getChannelType() {
        return this.channelType;
    }

    public String getSenderAvatar() {
        return this.senderAvatar;
    }

    public int getSenderId() {
        return this.senderId;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public int getSenderLevel() {
        return this.senderLevel;
    }

    public String getCmdId() {
        return this.cmdId;
    }

    public String getCmdName() {
        return this.cmdName;
    }

    public List<CommandNode> getCmdOptions() {
        return this.cmdOptions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(botId, msgId, roomAvatar, roomId, roomName, channelId, channelName, channelType, senderAvatar, senderId, senderName, senderLevel, cmdId, cmdName, cmdOptions);
    }

    @Override
    public int getId() {
        return this.senderId;
    }

    public record CommandNode(String name, int type, String value) { }
}
