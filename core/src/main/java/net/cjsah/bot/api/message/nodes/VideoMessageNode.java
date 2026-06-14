package net.cjsah.bot.api.message.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Map;

public class VideoMessageNode extends MessageNode {
    private final String file;
    private final String url;

    public VideoMessageNode(String file) {
        super(MessageNodeType.VIDEO);
        this.file = file;
        this.url = null;
    }

    public VideoMessageNode(JSONObject json) {
        super(MessageNodeType.VIDEO);
        this.file = this.parsetoString(json, "file", true);
        this.url = this.parsetoString(json, "url", true);
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("file", this.file);
    }

    @Override
    public String toString() {
        return this.toString("video", Map.of("file", this.file, "url", this.url));
    }

}
