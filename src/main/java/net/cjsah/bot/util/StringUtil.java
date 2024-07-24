package net.cjsah.bot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
                    append = false;
                    continue;
                case ';':
                    if (!append) {
                        append = true;
                        String text = extBuilder.toString();
                        extBuilder.setLength(0);
                        String value = NetMap.get(text);
                        if (value == null) {
                            log.warn("未知的网络转义字符'&{};', 作为普通字符串处理", text);
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
        return builder.toString();
    }

}
