package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
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
        this.file = json.getString("file");
        this.url = json.getString("url");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("file", this.file);
    }
}
