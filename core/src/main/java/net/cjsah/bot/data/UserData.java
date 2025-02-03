package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.Sex;
import net.cjsah.bot.util.EnumUtil;

public class UserData {
    protected final long userId;
    protected final String nickname;
    protected final Sex sex;
    protected final int age;

    public UserData(JSONObject raw) {
        this.userId = raw.getLongValue("user_id");
        this.nickname = raw.getString("nickname");
        this.sex = EnumUtil.ofName(Sex.class, raw.getString("sex"), Sex.UNKNOWN);
        this.age = raw.getIntValue("age");
    }

    public long getUserId() {
        return this.userId;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Sex getSex() {
        return this.sex;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }
}
