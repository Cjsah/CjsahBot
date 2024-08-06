package net.cjsah.bot.util;

import cn.hutool.core.stream.CollectorUtil;
import net.cjsah.bot.MainKt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StringUtil {
    private static final Map<String, String> NetMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

    static {
        NetMap.put("amp", "&");
        NetMap.put("#91", "[");
        NetMap.put("#93", "]");
        NetMap.put("#44", ",");
    }

    public static String netReplace(String str) {
        StringBuilder builder = new StringBuilder();
        StringBuilder extBuilder = new StringBuilder();
        boolean append = true;
        for (int i = 0; i < str.length(); i++) {
            int c = str.codePointAt(i);
            switch (c) {
                case '&':
                    if (append) {
                        append = false;
                    } else {
                        builder.append('&');
                        if (!extBuilder.isEmpty()) {
                            builder.append(extBuilder);
                            extBuilder.setLength(0);
                        }
                    }
                    continue;
                case ';':
                    if (!append) {
                        append = true;
                        String text = extBuilder.toString();
                        extBuilder.setLength(0);
                        String value = NetMap.get(text);
                        if (value == null) {
                            builder.append('&');
                            builder.append(text);
                            builder.append(';');
                        } else {
                            builder.append(value);
                        }
                        continue;
                    }
            }
            if (append) {
                builder.appendCodePoint(c);
            } else {
                extBuilder.appendCodePoint(c);
            }
        }
        if (!append) {
            builder.append('&');
            builder.append(extBuilder);
        }
        return builder.toString();
    }

    public static Collector<CharSequence, StringBuilder, String> join() {
        return new Collector<>() {
            @Override
            public Supplier<StringBuilder> supplier() {
                return () -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append("(");
                    return builder;
                };
            }

            @Override
            public BiConsumer<StringBuilder, CharSequence> accumulator() {
                return (builder, value) -> {
                    builder.append(value);
                    builder.append(",");
                };
            }

            @Override
            public BinaryOperator<StringBuilder> combiner() {
                return (r1, r2) -> r1;
            }

            @Override
            public Function<StringBuilder, String> finisher() {
                return builder -> {
                    builder.setCharAt(builder.length() - 1, ')');
                    return builder.toString();
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return CollectorUtil.CH_NOID;
            }
        };
    }
}
