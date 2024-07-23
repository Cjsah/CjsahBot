package net.cjsah.bot.resolver;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.util.EnumUtil;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class EnumDeserializer<T extends Enum<T>> implements ObjectReader<T> {
    private final String key;

    @SuppressWarnings("unchecked")
    @Override
    public T readObject(JSONReader reader, Type type, Object fieldName, long features) {
        if (reader.nextIfNull()) return null;
        if (type instanceof Class<?> clazz && clazz.isEnum()) {
            String value = reader.readString();
            Class<T> enumClass = (Class<T>) clazz;
            T[] enumConstants = enumClass.getEnumConstants();
            for (T constant : enumConstants) {
                if (EnumUtil.getFromKey(constant, this.key).equals(value)) {
                    return constant;
                }
            }
        }
        return null;
    }
}
