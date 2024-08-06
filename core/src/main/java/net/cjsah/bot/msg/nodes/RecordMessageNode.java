package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

import java.util.Map;

@Getter
public class RecordMessageNode extends MessageNode {
    private final String file;
    private final boolean magic;
    private final String url;

    /**
     *
     * @param file 见{@linkplain ImageMessageNode}
     * @param magic 是否变声
     */
    public RecordMessageNode(String file, boolean magic) {
        super(MessageType.RECORD);
        this.file = file;
        this.magic = magic;
        this.url = null;
    }

    public RecordMessageNode(JSONObject json) {
        super(MessageType.RECORD);
        this.file = this.parsetoString(json, "file", true);
        this.magic = json.getIntValue("magic") == 1;
        this.url = this.parsetoString(json, "url", true);
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("file", this.file);
        json.put("magic", this.magic ? 1 : 0);
    }

    @Override
    public String toString() {
        return this.toString("record", Map.of(
                "file", this.file,
                "magic", this.magic,
                "url", this.url
        ));
    }

}
