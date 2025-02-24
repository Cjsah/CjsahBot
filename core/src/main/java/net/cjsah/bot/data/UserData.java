package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.Sex;
import net.cjsah.bot.util.EnumUtil;

public class UserData {
    protected final String id;
    protected final String username;
    protected final String avatar;
    protected final boolean bot;
    protected final String unionOpenid;
    protected final String unionUserAccount;


    public UserData(JSONObject raw) {
        this.id = raw.getString("id");
        this.username = raw.getString("username");
        this.avatar = raw.getString("avatar");
        this.bot = raw.getBooleanValue("bot");
        this.unionOpenid = raw.getString("union_openid");
        this.unionUserAccount = raw.getString("union_user_account");
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public boolean isBot() {
        return this.bot;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", bot=" + bot +
                '}';
    }
}
