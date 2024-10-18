package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class UserMedal {
    private final int id;
    private final String name;
    private final String nameShort;
    private final String description;
    @Nullable
    private final Color color;
    private final String imgUrl;


    public UserMedal(JSONObject json) {
        this.id = json.getIntValue("medal_id");
        this.name = json.getString("name");
        this.nameShort = json.getString("name_short");
        this.description = json.getString("description");
        String color = json.getString("color");
        this.color = color.isEmpty() ? null : Color.decode(color);
        this.imgUrl = json.getString("img_url");
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getNameShort() {
        return this.nameShort;
    }

    public String getDescription() {
        return this.description;
    }

    @Nullable
    public Color getColor() {
        return this.color;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    @Override
    public String toString() {
        return "UserMedal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameShort='" + nameShort + '\'' +
                ", description='" + description + '\'' +
                ", color=" + color +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
