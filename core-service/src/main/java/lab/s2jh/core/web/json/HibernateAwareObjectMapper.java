package lab.s2jh.core.web.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {

    public HibernateAwareObjectMapper() {
        Hibernate4Module hibernate4Module = new Hibernate4Module();
        registerModule(hibernate4Module);

        SimpleModule myModule = new SimpleModule("MyModule", new Version(1, 0, 0, null, null, null));
        myModule.addSerializer(new EnumJsonSerializer());
        myModule.addSerializer(new DateTimeJsonSerializer());
        registerModule(myModule);

        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private static ObjectMapper objectMapper = new HibernateAwareObjectMapper();

    public static ObjectMapper getInstance() {
        return objectMapper;
    }
}
