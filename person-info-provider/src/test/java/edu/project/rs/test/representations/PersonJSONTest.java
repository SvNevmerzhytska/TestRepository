package edu.project.rs.test.representations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.project.rs.test.model.Person;
import edu.project.rs.test.utils.CustomDateDeserializer;
import edu.project.rs.test.utils.CustomDateSerializer;
import io.dropwizard.jackson.Jackson;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by s.nevmerzhytska on 1/29/2015.
 */
public class PersonJSONTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final String DATE_FOMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(DATE_FOMAT);
    private static final PersonJSON PERSON_JSON = new PersonJSON(new Person("Jane", "Test", DateTime.parse("31-01-1990", FORMATTER)));
    private static final String JSON_FILE = "fixtures/Person.json";

    @Before
    public void setUp() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(DateTime.class, new CustomDateDeserializer(FORMATTER));
        module.addSerializer(DateTime.class, new CustomDateSerializer(FORMATTER));
        MAPPER.registerModule(module);
    }

    @Test
    public void testSerializationToJSON() throws JsonProcessingException, IOException {
        assertThat(MAPPER.writeValueAsString(PERSON_JSON))
                .isEqualTo(MAPPER.readTree(fixture(JSON_FILE)).toString());
    }

    @Test
    public void testDeserilizationFromJSON() throws IOException {
        assertThat(MAPPER.readValue(fixture(JSON_FILE), PersonJSON.class))
                .isEqualTo(PERSON_JSON);
    }
}
