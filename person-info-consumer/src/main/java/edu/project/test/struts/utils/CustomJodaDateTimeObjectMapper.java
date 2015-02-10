package edu.project.test.struts.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by s.nevmerzhytska on 2/9/2015.
 */
public class CustomJodaDateTimeObjectMapper extends ObjectMapper {
    private DateTimeFormatter formatter;

    public CustomJodaDateTimeObjectMapper(String dateFormat) {
        super();
        SimpleModule module = new SimpleModule();
        formatter = DateTimeFormat.forPattern(dateFormat).withZoneUTC();
        module.addSerializer(DateTime.class, new CustomDateSerializer());
        module.addDeserializer(DateTime.class, new CustomDateDeserializer());
        registerModule(module);
    }

    public class CustomDateSerializer extends JsonSerializer<DateTime> {

        public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(formatter.print(dateTime));
        }
    }

    public class CustomDateDeserializer extends JsonDeserializer<DateTime> {

        public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return DateTime.parse((String) jsonParser.readValueAs(String.class), formatter);
        }
    }
}
