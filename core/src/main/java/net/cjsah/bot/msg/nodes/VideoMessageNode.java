package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

import java.util.Map;

@Getter
public class VideoMessageNode extends MessageNode {
    private final String file;
    private final String url;

    public VideoMessageNode(String file) {
        super(MessageType.VIDEO);
        this.file = file;
        this.url = null;
    }

    public VideoMessageNode(JSONObject json) {
        super(MessageType.VIDEO);
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
