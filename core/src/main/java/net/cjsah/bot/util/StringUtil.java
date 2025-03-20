package net.cjsah.bot.util;

import java.util.HashMap;
import java.util.Map;

public final class StringUtil {
    private static final Map<String, String> NetMap = new HashMap<>();

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
}
