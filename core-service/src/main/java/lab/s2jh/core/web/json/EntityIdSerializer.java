package lab.s2jh.core.web.json;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class EntityIdSerializer extends JsonSerializer<Persistable<? extends Serializable>> {

    @Override
    public void serialize(Persistable<? extends Serializable> value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        if (value != null) {
            jgen.writeStartObject();
            jgen.writeFieldName("id");
            jgen.writeString(ObjectUtils.toString(value.getId()));
            jgen.writeEndObject();
        }
    }
}
