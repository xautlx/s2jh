package lab.s2jh.core.web.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JacksonMapperFactory {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleModule myModule = new SimpleModule("MyModule", new Version(1, 0, 0, null));
        myModule.addSerializer(new EnumJsonSerializer());
        myModule.addSerializer(new DateTimeJsonSerializer());
        mapper.registerModule(myModule);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }
}
