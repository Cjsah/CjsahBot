package net.cjsah.bot.api.card.modules;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.card.AbstractCardModule;
import org.jetbrains.annotations.Nullable;

public class DividerCardModule extends AbstractCardModule {
    @Nullable
    private final String text;

    public DividerCardModule(@Nullable String text) {
        super("divider");
        this.text = text;
    }

    @Override
    public JSONObject generate() {
        JSONObject result = super.generate();
        if (this.text != null) {
            result.put("text", this.text);
        }
        return result;
    }
}
