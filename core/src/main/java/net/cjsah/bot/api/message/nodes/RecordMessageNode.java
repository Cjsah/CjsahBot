package net.cjsah.bot.api.message.nodes;

import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Map;

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
        super(MessageNodeType.RECORD);
        this.file = file;
        this.magic = magic;
        this.url = null;
    }

    public RecordMessageNode(JSONObject json) {
        super(MessageNodeType.RECORD);
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
        return this.pair("record", Map.of(
                "file", this.file,
                "magic", this.magic,
                "url", this.url
        ));
    }

}
