package net.cjsah.bot.resolver;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.util.EnumUtil;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class EnumSerializer<T extends Enum<T>> implements ObjectWriter<T> {
    private final String key;

    @SuppressWarnings("unchecked")
    @Override
    public void write(JSONWriter writer, Object object, Object fieldName, Type type, long features) {
        if (!object.getClass().isEnum()) {
            writer.writeNull();
            return;
        }
        T enumObj = (T) object;
        String value = EnumUtil.getFromKey(enumObj, this.key);
        writer.writeString(value);
    }
}
