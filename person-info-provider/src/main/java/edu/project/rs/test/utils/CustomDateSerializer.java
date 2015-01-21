package edu.project.rs.test.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Date;

/**
 * Created by s.nevmerzhytska on 1/19/2015.
 */
public class CustomDateSerializer extends JsonSerializer<DateTime> {

    public static DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");

    @Override
    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(formatter.print(dateTime));

    }
}

