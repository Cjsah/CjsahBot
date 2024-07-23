package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;
import org.jetbrains.annotations.Nullable;

@Getter
@ToString(callSuper = true)
public class ShareMessageNode extends MessageNode {
    private final String url;
    private final String title;
    private final String content;
    private final String imageUrl;

    public ShareMessageNode(String url, String title, @Nullable String content, @Nullable String imageUrl) {
        super(MessageType.SHARE);
        this.url = url;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("url", this.url);
        json.put("title", this.title);
        json.put("content", this.content);
        json.put("image", this.imageUrl);
    }
}
