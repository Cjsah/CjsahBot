package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.ChannelInfo;
import net.cjsah.bot.data.RoomInfo;
import net.cjsah.bot.data.UserInfo;
import net.cjsah.bot.event.Event;

public class CardButtonClickEvent extends Event {
    private final String msgId;
    private final String osType;
    private final RoomInfo roomInfo;
    private final ChannelInfo channelInfo;
    private final UserInfo senderInfo;
    private final String buttonText;
    private final String buttonValue;

    public CardButtonClickEvent(JSONObject json) {
        this.msgId = json.getString("msg_id");
        this.osType = json.getString("os_type");

        this.roomInfo = new RoomInfo(json.getJSONObject("room_base_info"));
        this.channelInfo = new ChannelInfo(json.getJSONObject("channel_base_info"));
        this.senderInfo = new UserInfo(json.getJSONObject("sender_info"), this.roomInfo.getId());

        this.buttonText = json.getString("text");
        this.buttonValue = json.getString("value");
    }

    public String getMsgId() {
        return this.msgId;
    }

    public String getOsType() {
        return this.osType;
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

    public String getButtonText() {
        return this.buttonText;
    }

    public String getButtonValue() {
        return this.buttonValue;
    }

    @Override
    public String toString() {
        return "CardButtonClickEvent{" +
                "msgId='" + msgId + '\'' +
                ", osType='" + osType + '\'' +
                ", roomInfo=" + roomInfo +
                ", channelInfo=" + channelInfo +
                ", senderInfo=" + senderInfo +
                ", buttonText='" + buttonText + '\'' +
                ", buttonValue='" + buttonValue + '\'' +
                '}';
    }
}
