package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
public class ImageMessageNode extends MessageNode {
    private final String file;
    private final boolean isFlash;
    private final String url;

    /**
     * @param file
     * <ul>
     *     <li>绝对路径，例如 file:///C:\\Users\xxx\Pictures\1.png，格式使用 file URI</li>
     *     <li>网络 URL，例如 http://img.img.com/12345.jpg</li>
     *     <li>Base64 编码，例如 base64://abcde==</li>
     * </ul>
     * @param isFlash 是否为闪照
     */
    public ImageMessageNode(String file, boolean isFlash) {
        super(MessageType.IMAGE);
        this.file = file;
        this.isFlash = isFlash;
        this.url = null;
    }

    public ImageMessageNode(JSONObject json) {
        super(MessageType.IMAGE);
        this.file = json.getString("file");
        this.isFlash = "flush".equals(json.getString("type"));
        this.url = json.getString("url");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("file", this.file);
        if (this.isFlash) {
            json.put("type", "flush");
        }
    }
}
