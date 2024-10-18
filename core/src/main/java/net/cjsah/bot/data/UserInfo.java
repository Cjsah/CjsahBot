package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public class UserInfo {
    private final int id;
    private final String nickname;
    private final boolean bot;
    private final int level;
    private final UserAvatar avatar;
    private final UserTag tag;
    private final List<UserMedal> medals;

    public UserInfo(JSONObject json) {
        this.id = json.getIntValue("user_id");
        this.nickname = json.getString("nickname");
        this.bot = json.getBoolean("bot");
        this.level = json.getIntValue("level");
        this.avatar = new UserAvatar(json);
        JSONObject tag = json.getJSONObject("tag");
        this.tag = tag == null ? null : new UserTag(tag);
        List<JSONObject> medals = json.getList("medals", JSONObject.class);
        this.medals = medals == null ? List.of() : medals.stream().map(UserMedal::new).toList();
    }

    public int getId() {
        return this.id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public boolean isBot() {
        return this.bot;
    }

    public int getLevel() {
        return this.level;
    }

    public UserAvatar getAvatar() {
        return this.avatar;
    }

    public UserTag getTag() {
        return this.tag;
    }

    public List<UserMedal> getMedals() {
        return this.medals;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", bot=" + bot +
                ", level=" + level +
                ", avatar=" + avatar +
                ", tag=" + tag +
                ", medals=" + medals +
                '}';
    }
}
