package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

import java.awt.*;

public class UserTag {
    private final Color bgColor;
    private final Color textColor;
    private final String text;

    public UserTag(JSONObject json) {
        this.bgColor = Color.decode(json.getString("bg_color"));
        this.textColor = Color.decode(json.getString("text_color"));
        this.text = json.getString("text");
    }

    public Color getBgColor() {
        return this.bgColor;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public String getText() {
        return this.text;
    }
}
