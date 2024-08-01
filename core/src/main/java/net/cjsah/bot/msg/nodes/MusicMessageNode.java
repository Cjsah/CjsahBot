package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.data.enums.MusicType;
import net.cjsah.bot.msg.MessageNode;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
public class MusicMessageNode extends MessageNode {
    private final MusicType musicType;
    /* type == qq|163|xm */
    private final long id;
    /* type == custom */
    private final String url;
    private final String audio;
    private final String title;
    private final String content;
    private final String imageUrl;

    public MusicMessageNode(MusicType musicType, long musicId) {
        super(MessageType.MUSIC);
        if (musicType == MusicType.CUSTOM) {
            throw new IllegalArgumentException("Custom music require url!");
        }
        this.musicType = musicType;
        this.id = musicId;
        this.url = null;
        this.audio = null;
        this.title = null;
        this.content = null;
        this.imageUrl = null;
    }

    public MusicMessageNode(String url, String audio, String title, @Nullable String content, @Nullable String imageUrl) {
        super(MessageType.MUSIC);
        this.musicType = MusicType.CUSTOM;
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
        this.musicType = MusicType.fromName(type);
        if (this.musicType == MusicType.CUSTOM) {
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
        json.put("type", this.musicType.getValue());
        if (this.musicType == MusicType.CUSTOM) {
            json.put("url", this.url);
            json.put("audio", this.audio);
            json.put("title", this.title);
            json.put("content", this.content);
            json.put("imageUrl", this.imageUrl);
        } else {
            json.put("id", String.valueOf(this.id));
        }
    }

    @Override
    public String toString() {
        if (this.musicType == MusicType.CUSTOM) {
            return this.toString("music", Map.of(
                    "type", this.musicType.getValue(),
                    "url", this.url,
                    "audio", this.audio,
                    "title", this.title,
                    "content", this.content,
                    "imageUrl", this.imageUrl
            ));
        } else {
            return this.toString("music", this.id);
        }

    }

}
