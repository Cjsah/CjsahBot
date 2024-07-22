package net.cjsah.bot.parser;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ReceivedMsgParser {
    private static final Logger log = LoggerFactory.getLogger(ReceivedMsgParser.class);
    private final boolean isLast;
    private final String nextKey;
    private final Consumer<JSONObject> run;
    private final Map<String, ReceivedMsgParser> subParser = new HashMap<>();

    protected ReceivedMsgParser(boolean isLast, String nextKey, Consumer<JSONObject> run) {
        this.isLast = isLast;
        this.nextKey = nextKey;
        this.run = run;
    }

    protected void addParser(String value, ReceivedMsgParser parser) {
        this.subParser.put(value, parser);
    }

    public void parse(JSONObject raw) {
        if (this.isLast) {
            this.run.accept(raw);
            return;
        }
        String node = raw.getString(this.nextKey);
        ReceivedMsgParser parser = this.subParser.get(node);
        if (parser != null) {
            parser.parse(raw);
        } else {
            log.warn("Unknown node: {}", raw);
        }
    }
}
