package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

public class AnonymousUserData {
    private final long id;
    private final String name;
    private final String flag;

    public AnonymousUserData(JSONObject raw) {
        this.id = raw.getLongValue("id");
        this.name = raw.getString("name");
        this.flag = raw.getString("flag");
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFlag() {
        return this.flag;
    }

    @Override
    public String toString() {
        return "AnonymousUserData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
