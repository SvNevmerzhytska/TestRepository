package edu.project.rs.test.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import edu.project.rs.test.ApplicationConfiguration;
import edu.project.rs.test.MyApplication;
import edu.project.rs.test.MyTestAppSpringConfiguration;
import edu.project.rs.test.exceptions.NotFoundException;
import edu.project.rs.test.representations.PersonJSON;
import edu.project.rs.test.utils.CustomDateDeserializer;
import edu.project.rs.test.utils.CustomDateSerializer;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by s.nevmerzhytska on 2/2/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MyTestAppSpringConfiguration.class})
public class PersonResourceIntegraionTest {
    @Rule
    public final DropwizardAppRule<ApplicationConfiguration> RULE =
            new DropwizardAppRule<ApplicationConfiguration>(MyApplication.class, "dropwizard-config.yml");

    private static Client client;
    private static final String PERSON_RESOURCE_PATH = "/persons";
    private static final String HOST_URL = "http://localhost";
    private static String PERSON_RESOURCE_URL;

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final String JSON_FILES_LOCATION = "fixtures/";

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() {
        RULE.getConfiguration().setSpringConfiguration(MyTestAppSpringConfiguration.class);

        DateTimeFormatter formatter = RULE.getConfiguration().getDateTimeFormatter();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(DateTime.class, new CustomDateDeserializer(formatter));
        module.addSerializer(DateTime.class, new CustomDateSerializer(formatter));
        MAPPER.registerModule(module);

        client = new Client();

        PERSON_RESOURCE_URL = HOST_URL + ":" + RULE.getLocalPort() + PERSON_RESOURCE_PATH;
    }

    @Test
    public void testAddPersonOk() throws IOException {
        ClientResponse response = client.resource(PERSON_RESOURCE_URL).header("Content-Type", "application/json")
                .post(ClientResponse.class, fixture(JSON_FILES_LOCATION + "Person1.json"));
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity(Integer.class)).isEqualTo(1);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL).get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        assertThat(checkResponse.getEntity(new GenericType<List>(){}).size()).isEqualTo(1);
    }

    @Test
    public void testAddPersonInvalid() {
        String invalidPersonJSON = "{\"firstName\":\"Test\",\"birthDate\":\"00-00-1999\"}";
        ClientResponse response = client.resource(PERSON_RESOURCE_URL).header("Content-Type", "application/json")
                .post(ClientResponse.class, invalidPersonJSON);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testAddPersonSeveralTime() throws IOException {
        ClientResponse response = client.resource(PERSON_RESOURCE_URL).header("Content-Type", "application/json")
                .post(ClientResponse.class, fixture(JSON_FILES_LOCATION + "Person1.json"));
        assertThat(response.getStatus()).isEqualTo(200);

        assertThat(response.getEntity(Integer.class)).isEqualTo(1);
        response = client.resource(PERSON_RESOURCE_URL).header("Content-Type", "application/json")
                .post(ClientResponse.class, fixture(JSON_FILES_LOCATION + "Person1.json"));
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void testGetPersonByIdOk() throws IOException {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/1")
                .get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity(String.class))
                .isEqualTo(MAPPER.readTree(fixture(JSON_FILES_LOCATION + "/Person1.json")).toString());

        response = client.resource(PERSON_RESOURCE_URL + "/2")
                .get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity(String.class))
                .isEqualTo(MAPPER.readTree(fixture(JSON_FILES_LOCATION + "/Person2.json")).toString());

        response = client.resource(PERSON_RESOURCE_URL + "/3")
                .get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity(String.class))
                .isEqualTo(MAPPER.readTree(fixture(JSON_FILES_LOCATION + "/Person3.json")).toString());
    }

    @Test
    public void testGetPersonByInvalitId() {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/invalid").get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetPersonByNonExistentId() {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/10").get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getEntity(String.class)).isEqualTo(NotFoundException.PERSON_NOT_FOUND);
    }

    @Test
    public void testGetPersons() throws IOException {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL).get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity(new GenericType<List>(){}).size()).isEqualTo(3);
    }

    @Test
    public void testUpdatePersonOk() throws IOException {
        populateDB();

        PersonJSON oldPerson = MAPPER.readValue(fixture(JSON_FILES_LOCATION + "/Person1.json"), PersonJSON.class);
        PersonJSON newPerson = MAPPER.readValue(fixture(JSON_FILES_LOCATION + "/Person.json"), PersonJSON.class);

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/1").header("Content-Type", "application/json")
                .put(ClientResponse.class, fixture(JSON_FILES_LOCATION + "/Person.json"));
        assertThat(response.getStatus()).isEqualTo(204);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL + "/1")
                .get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        PersonJSON checkPerson = MAPPER.readValue(checkResponse.getEntity(String.class), PersonJSON.class);
        assertEquals("Person should be updated", newPerson, checkPerson);
        assertNotEquals("Person should not be equal to old one", checkPerson, oldPerson);
    }

    @Test
    public void testUpdatePersonInvalidId() throws IOException {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/invalid")
                .header("Content-Type", "application/json")
                .put(ClientResponse.class, fixture(JSON_FILES_LOCATION + "/Person.json"));
        assertThat(response.getStatus()).isEqualTo(400);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL).get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        assertThat(checkResponse.getEntity(new GenericType<List>(){}).size()).isEqualTo(3);
    }

    @Test
    public void testUpdatePersonInvalidJSON() throws IOException {
        populateDB();

        PersonJSON oldPerson = MAPPER.readValue(fixture(JSON_FILES_LOCATION + "/Person1.json"), PersonJSON.class);

        String invalidPersonJSON = "{\"firstName\":\"Test\",\"birthDate\":\"00-00-1999\"}";
        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/1").header("Content-Type", "application/json")
                .put(ClientResponse.class, invalidPersonJSON);
        assertThat(response.getStatus()).isEqualTo(400);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL + "/1").get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        PersonJSON checkPerson = MAPPER.readValue(checkResponse.getEntity(String.class), PersonJSON.class);
        assertEquals("Person should not be changed", oldPerson, checkPerson);
    }

    @Test
    public void testUpdatePersonNonExistentId() throws IOException {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/10").header("Content-Type", "application/json")
                .put(ClientResponse.class, fixture(JSON_FILES_LOCATION + "/Person.json"));
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getEntity(String.class)).isEqualTo(NotFoundException.PERSON_NOT_FOUND);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL).get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        assertThat(checkResponse.getEntity(new GenericType<List>(){}).size()).isEqualTo(3);
    }

    @Test
    public void testUpdatePersonViolationConstraint() throws IOException {
        populateDB();

        PersonJSON firstPerson = MAPPER.readValue(fixture(JSON_FILES_LOCATION + "/Person1.json"), PersonJSON.class);
        PersonJSON secondPerson = MAPPER.readValue(fixture(JSON_FILES_LOCATION + "/Person2.json"), PersonJSON.class);

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/2").header("Content-Type", "application/json")
                .put(ClientResponse.class, fixture(JSON_FILES_LOCATION + "/Person1.json"));
        assertThat(response.getStatus()).isEqualTo(422);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL + "/1").get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        PersonJSON checkPerson = MAPPER.readValue(checkResponse.getEntity(String.class), PersonJSON.class);
        assertEquals("First person should not be changed", firstPerson, checkPerson);
        checkResponse = client.resource(PERSON_RESOURCE_URL + "/2").get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        checkPerson = MAPPER.readValue(checkResponse.getEntity(String.class), PersonJSON.class);
        assertEquals("Second person should not be changed", secondPerson, checkPerson);
    }

    @Test
    public void testDeletePersonOk() {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/1")
                .delete(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(204);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL).get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        assertThat(checkResponse.getEntity(new GenericType<List>(){}).size()).isEqualTo(2);
        checkResponse = client.resource(PERSON_RESOURCE_URL + "/1").get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(404);
        assertThat(checkResponse.getEntity(String.class)).isEqualTo(NotFoundException.PERSON_NOT_FOUND);
    }

    @Test
    public void testDeletePersonInvalidId() {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/invalid")
                .delete(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(400);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL).get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        assertThat(checkResponse.getEntity(new GenericType<List>(){}).size()).isEqualTo(3);
    }

    @Test
    public void testDeletePersonNonExistentId() {
        populateDB();

        ClientResponse response = client.resource(PERSON_RESOURCE_URL + "/10")
                .delete(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getEntity(String.class)).isEqualTo(NotFoundException.PERSON_NOT_FOUND);

        ClientResponse checkResponse = client.resource(PERSON_RESOURCE_URL).get(ClientResponse.class);
        assertThat(checkResponse.getStatus()).isEqualTo(200);
        assertThat(checkResponse.getEntity(new GenericType<List>(){}).size()).isEqualTo(3);
    }

    private void populateDB() {
        ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
        rdp.addScript(new ClassPathResource("insert-test-data.sql"));
        try (Connection connection = dataSource.getConnection()) {
            rdp.populate(connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
