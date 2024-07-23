package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.data.enums.MusicType;
import net.cjsah.bot.msg.MessageNode;
import org.jetbrains.annotations.Nullable;

@Getter
@ToString(callSuper = true)
public class MusicMessageNode extends MessageNode {
    private final MusicType type;
    /* type == qq|163|xm */
    private final long id;
    /* type == custom */
    private final String url;
    private final String audio;
    private final String title;
    private final String content;
    private final String imageUrl;

    public MusicMessageNode(MusicType type, long musicId) {
        super(MessageType.MUSIC);
        if (type == MusicType.CUSTOM) {
            throw new IllegalArgumentException("Custom music require url!");
        }
        this.type = type;
        this.id = musicId;
        this.url = null;
        this.audio = null;
        this.title = null;
        this.content = null;
        this.imageUrl = null;
    }

    public MusicMessageNode(String url, String audio, String title, @Nullable String content, @Nullable String imageUrl) {
        super(MessageType.MUSIC);
        this.type = MusicType.CUSTOM;
        this.id = 0;
        this.url = url;
        this.audio = audio;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public MusicMessageNode(JSONObject json) {
        super(MessageType.MUSIC);
        String type = json.getString("type");
        this.type = MusicType.fromName(type);
        if (this.type == MusicType.CUSTOM) {
            this.id = 0;
            this.url = json.getString("url");
            this.audio = json.getString("audio");
            this.title = json.getString("title");
            this.content = json.getString("content");
            this.imageUrl = json.getString("imageUrl");
        } else {
            this.id = this.parseToLong(json, "id");
            this.url = null;
            this.audio = null;
            this.title = null;
            this.content = null;
            this.imageUrl = null;
        }
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("type", this.type.getValue());
        if (this.type == MusicType.CUSTOM) {
            json.put("id", String.valueOf(this.id));
        } else {
            json.put("url", this.url);
            json.put("audio", this.audio);
            json.put("title", this.title);
            json.put("content", this.content);
            json.put("imageUrl", this.imageUrl);
        }
    }
}
