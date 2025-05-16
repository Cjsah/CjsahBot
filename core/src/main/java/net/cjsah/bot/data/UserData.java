package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

public class UserData {
    private final String id;
    private final String memberOpenid;
    private final String unionOpenid;

    public UserData(JSONObject raw, boolean isGroup) {
        this.id = raw.getString("id");
        this.memberOpenid = raw.getString(isGroup ? "member_openid" : "user_openid");
        this.unionOpenid = raw.getString("union_openid");
    }

    public String getId() {
        return this.memberOpenid;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id='" + id + '\'' +
                ", memberOpenid='" + memberOpenid + '\'' +
                ", unionOpenid='" + unionOpenid + '\'' +
                '}';
    }
}
