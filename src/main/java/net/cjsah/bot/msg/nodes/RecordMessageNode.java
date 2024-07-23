package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
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
        this.file = json.getString("file");
        this.magic = json.getIntValue("magic") == 1;
        this.url = json.getString("url");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("file", this.file);
        json.put("magic", this.magic ? 1 : 0);
    }
}
