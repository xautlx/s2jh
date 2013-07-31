package lab.s2jh.core.web.json;

import java.io.IOException;
import java.lang.reflect.Field;

import lab.s2jh.core.annotation.MetaData;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class EnumJsonSerializer extends JsonSerializer<Enum> {

    @Override
    public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeFieldName("ordinal");
        jgen.writeNumber(value.ordinal());
        jgen.writeFieldName("name");
        jgen.writeString(value.name());
        //TODO:加入Cache优化性能
        Field enumField = FieldUtils.getDeclaredField(value.getClass(), value.name());
        MetaData entityComment = enumField.getAnnotation(MetaData.class);
        if (entityComment != null) {
            jgen.writeFieldName("title");
            jgen.writeString(entityComment.title());
        }
        jgen.writeEndObject();
    }

    public Class<Enum> handledType() {
        return Enum.class;
    }
}
