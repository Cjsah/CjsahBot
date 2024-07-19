package net.cjsah.bot.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ReceivedMsgParser {
    private static final Logger log = LoggerFactory.getLogger(ReceivedMsgParser.class);
    private final boolean isLast;
    private final String nextKey;
    private final Function<JsonNode, Object> nextProcess;
    private final Consumer<ObjectNode> run;
    private final Map<Object, ReceivedMsgParser> subParser = new HashMap<>();

    protected ReceivedMsgParser(boolean isLast, String nextKey, Function<JsonNode, Object> nextProcess, Consumer<ObjectNode> run) {
        this.isLast = isLast;
        this.nextKey = nextKey;
        this.nextProcess = nextProcess;
        this.run = run;
    }

    protected void addParser(Object value, ReceivedMsgParser parser) {
        this.subParser.put(value, parser);
    }

    public void parse(ObjectNode raw) {
        if (this.isLast) {
            this.run.accept(raw);
            return;
        }
        JsonNode node = raw.get(this.nextKey);
        Object obj = nextProcess.apply(node);
        ReceivedMsgParser parser = this.subParser.get(obj);
        if (parser != null) {
            parser.parse(raw);
        } else {
            log.warn("Unknown node: {}", raw);
        }
    }
}
