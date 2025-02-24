package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.GroupRole;
import net.cjsah.bot.util.EnumUtil;

public class GroupUserData extends UserData {
    protected final String card;
    protected final String area;
    protected final String level;
    protected final GroupRole role;
    protected final String title;

    public GroupUserData(JSONObject raw) {
        super(raw);
        this.card = raw.getString("card");
        this.area = raw.getString("area");
        this.level = raw.getString("level");
        this.role = EnumUtil.ofName(GroupRole.class, raw.getString("role"));
        this.title = raw.getString("title");
    }

    public String getCard() {
        return this.card;
    }

    public String getArea() {
        return this.area;
    }

    public String getLevel() {
        return this.level;
    }

    public GroupRole getRole() {
        return this.role;
    }

    public String getTitle() {
        return this.title;
    }

}
