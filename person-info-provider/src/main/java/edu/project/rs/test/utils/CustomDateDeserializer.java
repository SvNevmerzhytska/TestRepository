package edu.project.rs.test.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by s.nevmerzhytska on 1/19/2015.
 */
public class CustomDateDeserializer extends JsonDeserializer<DateTime> {

    private DateTimeFormatter formatter;

    public CustomDateDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return DateTime.parse(jsonParser.readValueAs(String.class), formatter);
    }
}

