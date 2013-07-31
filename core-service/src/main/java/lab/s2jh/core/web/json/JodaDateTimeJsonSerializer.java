package lab.s2jh.core.web.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JodaDateTimeJsonSerializer extends JsonSerializer<DateTime> {

    public static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        if (value != null) {
            jgen.writeString(DATE_FORMAT.format(value.toDate()));
        }

    }

}
