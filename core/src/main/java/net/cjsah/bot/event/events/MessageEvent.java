package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.Event;

public class MessageEvent extends Event {
    private final String avatar;
    private final boolean bot;

    private final String roomId;
    private final String roomName;

    private final String channelId;
    private final String channelName;
    private final int channelType;

    private final int userId;
    private final String userName;

    private final int level;

    private final String msg;
    private final String msgId;
    private final int msgType;

    public MessageEvent(JSONObject json) {
        this.avatar = json.getString("avatar");
        this.bot = json.getBooleanValue("bot");
        this.roomId = json.getString("room_id");
        this.roomName = json.getString("room_nickname");
        this.channelId = json.getString("channel_id");
        this.channelName = json.getString("channel_name");
        this.channelType = json.getIntValue("channel_type");
        this.userId = json.getIntValue("user_id");
        this.userName = json.getString("nickname");
        this.level = json.getIntValue("level");
        this.msg = json.getString("msg");
        this.msgId = json.getString("msg_id");
        this.msgType = json.getIntValue("msg_type");
    }

    public String getAvatar() {
        return this.avatar;
    }

    public boolean isBot() {
        return this.bot;
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

    public int getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getLevel() {
        return this.level;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public int getMsgType() {
        return this.msgType;
    }
}
