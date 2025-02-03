package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.MessageNodeType;
import net.cjsah.bot.util.MsgUtil;

import java.util.Map;

public class ImageMessageNode extends MessageNode {
    private final String file;
    private final boolean isFlash;
    private final String url;
    private final String local;

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
        super(MessageNodeType.IMAGE);
        this.file = file;
        this.isFlash = isFlash;
        this.url = null;
        this.local = null;
    }

    public ImageMessageNode(JSONObject json) {
        super(MessageNodeType.IMAGE);
        this.file = this.parsetoString(json, "file", true);
        this.isFlash = "flush".equals(json.getString("type"));
        this.url = this.parsetoString(json, "url", true);
        this.local = MsgUtil.saveImage(this.url);
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("file", this.file);
        if (this.isFlash) {
            json.put("type", "flush");
        }
    }

    @Override
    public String toString() {
        return this.toString("image", Map.of(
                "file", this.file,
                "flash", this.isFlash
        ));
    }

}
