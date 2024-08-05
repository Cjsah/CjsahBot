package net.cjsah.bot.parser;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.MainKt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ReceivedEventParserNode {
    private final boolean isLast;
    private final String nextKey;
    private final Consumer<JSONObject> run;
    private final Map<String, ReceivedEventParserNode> subParser = new HashMap<>();

    protected ReceivedEventParserNode(boolean isLast, String nextKey, Consumer<JSONObject> run) {
        this.isLast = isLast;
        this.nextKey = nextKey;
        this.run = run;
    }

    protected void addParser(String value, ReceivedEventParserNode parser) {
        this.subParser.put(value, parser);
    }

    public void parse(JSONObject raw) {
        if (this.isLast) {
            this.run.accept(raw);
            return;
        }
        String node = raw.getString(this.nextKey);
        ReceivedEventParserNode parser = this.subParser.get(node);
        if (parser != null) {
            parser.parse(raw);
        } else {
            MainKt.getLog().warn("Unknown node: {}", raw);
        }
    }
}
