package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.cache.RoleCache;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UserInfo {
    private final int id;
    private final String nickname;
    private final boolean bot;
    private final int level;
    private final UserAvatar avatar;
    private final UserTag tag;
    private final List<UserMedal> medals;
    private final List<RoleInfo> roles;

    public UserInfo(JSONObject json) {
        this(json, null);
    }

    public UserInfo(JSONObject json, @Nullable String roomId) {
        this.id = json.getIntValue("user_id");
        this.nickname = json.getString("nickname");
        this.bot = json.getBoolean("bot");
        this.level = json.getIntValue("level");
        this.avatar = new UserAvatar(json);
        JSONObject tag = json.getJSONObject("tag");
        this.tag = tag == null ? null : new UserTag(tag);
        List<JSONObject> medals = json.getList("medals", JSONObject.class);
        this.medals = medals == null ? List.of() : medals.stream().map(UserMedal::new).toList();
        List<String> roles = json.getList("roles", String.class);
        this.roles = (roomId == null || roles == null) ? List.of() : RoleCache.getRole(roomId, roles);
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

    public List<RoleInfo> getRoles() {
        return this.roles;
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
