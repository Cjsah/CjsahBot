package net.cjsah.bot.resolver;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.util.EnumUtil;

import java.io.IOException;

@RequiredArgsConstructor
public class EnumSerializer<T extends Enum<T>> extends JsonSerializer<T> implements ContextualSerializer {
    private final Class<T> clazz;
    private final String key;

    public EnumSerializer() {
        this.clazz = null;
        this.key = null;
    }

    @Override
    public void serialize(T value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (this.clazz == null || this.key == null) return;
        generator.writeString(EnumUtil.getFromKey(value, this.key));
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) {
        String key = property.getAnnotation(JsonEnumMapping.class).value();
        Class<?> clazz = property.getType().getRawClass();
        return new EnumSerializer<>((Class<T>) clazz, key);
    }
}
