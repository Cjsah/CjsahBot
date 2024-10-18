package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

public class MemeInfo {
    private final String name;
    private final String path;
    private final String ext;
    private final long createTime;
    private final MemeType memeType;

    public MemeInfo(JSONObject json) {
        this.name = json.getString("name");
        this.path = json.getString("path");
        this.ext = json.getString("ext");
        this.createTime = json.getLongValue("create_time");
        this.memeType = MemeType.of(json.getIntValue("meme_type"));
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getExt() {
        return this.ext;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public MemeType getMemeType() {
        return this.memeType;
    }
}
