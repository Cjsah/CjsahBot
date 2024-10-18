package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

import java.awt.*;

public class UserAvatar {
    private final String avatarUrl;
    private final String decorationUrl;

    public UserAvatar(JSONObject json) {
        this.avatarUrl = json.getString("avatar");
        this.decorationUrl = json.getJSONObject("avatar_decoration").getString("src_url");
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public String getDecorationUrl() {
        return this.decorationUrl;
    }

    @Override
    public String toString() {
        return "UserAvatar{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", decorationUrl='" + decorationUrl + '\'' +
                '}';
    }
}
