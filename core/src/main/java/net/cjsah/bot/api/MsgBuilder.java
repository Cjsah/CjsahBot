package net.cjsah.bot.api;

import cn.hutool.core.util.IdUtil;

public class MsgBuilder {
    private final String uuid;
    private final String roomId;
    private final String channelId;
    private final String msg;
    private String at;
    private String replay;

    public MsgBuilder(String roomId, String channelId, String msg) {
        this.uuid = IdUtil.fastSimpleUUID();
        this.roomId = roomId;
        this.channelId = channelId;
        this.msg = msg;
        this.at = "";
        this.replay = "";
    }

    public MsgBuilder at(int at) {
        this.at = String.valueOf(at);
        return this;
    }

    public MsgBuilder replay(String replay) {
        this.replay = replay;
        return this;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getAt() {
        return this.at;
    }

    public String getReplay() {
        return this.replay;
    }

    @Override
    public String toString() {
        return "MsgBuilder{" +
                "roomId='" + roomId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", msg='" + msg + '\'' +
                ", at='" + at + '\'' +
                ", replay='" + replay + '\'' +
                '}';
    }
}
