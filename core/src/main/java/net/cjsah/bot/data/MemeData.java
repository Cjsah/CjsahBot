package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

public class MemeData {
    private final UserInfo userInfo;
    private final MemeInfo memeInfo;

    public MemeData(JSONObject json) {
        this.userInfo = new UserInfo(json.getJSONObject("user_info"));
        this.memeInfo = new MemeInfo(json.getJSONObject("meme_info"));
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public MemeInfo getMemeInfo() {
        return this.memeInfo;
    }


}
