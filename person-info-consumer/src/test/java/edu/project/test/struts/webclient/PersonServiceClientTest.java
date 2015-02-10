package edu.project.test.struts.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;
import edu.project.test.struts.exceptions.ExternalServerError;
import edu.project.test.struts.exceptions.InvalidRequestException;
import edu.project.test.struts.model.Person;
import edu.project.test.struts.utils.ApplicationContextLoader;
import edu.project.test.struts.utils.CustomJodaDateTimeObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.github.restdriver.clientdriver.RestClientDriver.*;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.*;

/**
 * Created by s.nevmerzhytska on 2/5/2015.
 */
public class PersonServiceClientTest {

    @Rule
    public ClientDriverRule personDriver = new ClientDriverRule(8080);

    private static PersonServiceClient personServiceClient;

    private static final String RESOURCE_URL = "/persons";
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy").withZoneUTC();
    private static final ObjectMapper MAPPER = new CustomJodaDateTimeObjectMapper("dd-MM-yyyy");

    private static List<Person> mockPersons = new ArrayList();
    private static Person firstPerson;// = new Person(1, "TestName", "TestSurname", DateTime.parse("01-01-1993", FORMATTER));
    private static Person testPerson = new Person("NewName", "NewSurname", DateTime.parse("01-08-1989", FORMATTER));

    @Before
    public void setUpBeforeClass() {
        firstPerson = new Person(1, "TestName", "TestSurname", DateTime.parse("01-01-1993", FORMATTER));
        mockPersons.add(firstPerson);
        Person secondPerson = new Person(2, "TestName2", "TestSurname2", DateTime.parse("28-02-1990", FORMATTER));
        mockPersons.add(secondPerson);

        personServiceClient = new PersonServiceClient();
        ApplicationContextLoader loader = new ApplicationContextLoader();
        loader.load(personServiceClient, "spring/applicationContext.xml");
    }

    @Test
    public void testPostPersonOk() throws JsonProcessingException {
        personDriver.addExpectation(onRequestTo(RESOURCE_URL)
                        .withBody(MAPPER.writeValueAsString(testPerson), MediaType.APPLICATION_JSON)
                        .withMethod(Method.POST),
                giveResponse(Integer.toString(mockPersons.size() + 1), MediaType.TEXT_PLAIN)
                        .withStatus(Response.Status.OK.getStatusCode()));

        int resultId = personServiceClient.postPerson(testPerson);

        assertTrue("Person should be successfully added (id > 0)", resultId > 0);
    }

    @Test (expected = InvalidRequestException.class)
    public void testPostPersonSeveralTimes() throws JsonProcessingException {
        personDriver.addExpectation(onRequestTo(RESOURCE_URL)
                        .withBody(MAPPER.writeValueAsString(testPerson), MediaType.APPLICATION_JSON)
                        .withMethod(Method.POST),
                giveResponse(Integer.toString(mockPersons.size() + 1), MediaType.TEXT_PLAIN)
                        .withStatus(Response.Status.OK.getStatusCode()));

        int resultId = personServiceClient.postPerson(testPerson);

        assertTrue("Person should be successfully added (id > 0)", resultId > 0);

        personDriver.addExpectation(onRequestTo(RESOURCE_URL)
                        .withBody(MAPPER.writeValueAsString(testPerson), MediaType.APPLICATION_JSON)
                        .withMethod(Method.POST),
                giveEmptyResponse().withStatus(422));

        personServiceClient.postPerson(testPerson);
    }

    @Test (expected = ExternalServerError.class)
    public void testPostPersonExternalServerProblem() {
        personDriver.addExpectation(onRequestTo(RESOURCE_URL).withMethod(Method.POST),
                giveEmptyResponse().withStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));

        personServiceClient.postPerson(testPerson);
    }

    @Test
    public void testGetPersonListOk() throws JsonProcessingException {
        personDriver.addExpectation(onRequestTo(RESOURCE_URL).withMethod(Method.GET),
                giveResponse(MAPPER.writeValueAsString(mockPersons), MediaType.APPLICATION_JSON)
                        .withStatus(OK.getStatusCode()));

        List<Person> personList = personServiceClient.getPersonList();

        assertFalse("List of persons should not pe empty", personList.isEmpty());
    }

    @Test (expected = ExternalServerError.class)
    public void testGetPersonListExternalServerProblem() {
        personDriver.addExpectation(onRequestTo(RESOURCE_URL).withMethod(Method.GET),
                giveEmptyResponse().withStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));

        personServiceClient.getPersonList();
    }

    @Test
    public void testUpdatePersonOk() throws JsonProcessingException {
        testPerson.id = 1;
        personDriver.addExpectation(onRequestTo(RESOURCE_URL + "/1")
                        .withBody(MAPPER.writeValueAsString(testPerson), MediaType.APPLICATION_JSON)
                        .withMethod(Method.PUT),
                giveEmptyResponse().withStatus(Response.Status.NO_CONTENT.getStatusCode()));

        boolean result = personServiceClient.updatePerson(testPerson);

        assertTrue("Updating of person should be successful", result);
    }

    @Test (expected = InvalidRequestException.class)
    public void testUpdatePersonViolatedConstraint() throws JsonProcessingException {
        firstPerson.id = 2;
        personDriver.addExpectation(onRequestTo(RESOURCE_URL + "/2")
                        .withBody(MAPPER.writeValueAsString(firstPerson), MediaType.APPLICATION_JSON)
                        .withMethod(Method.PUT),
                giveEmptyResponse().withStatus(422));

        boolean result = personServiceClient.updatePerson(firstPerson);
    }

    @Test (expected = ExternalServerError.class)
    public void testUpdatePersonExternalServerProblem() {
        personDriver.addExpectation(onRequestTo(RESOURCE_URL + "/1").withMethod(Method.PUT),
                giveEmptyResponse().withStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));

        personServiceClient.updatePerson(firstPerson);
    }

    @Test
    public void testDeletePersonOk() {
        personDriver.addExpectation(onRequestTo(RESOURCE_URL + "/1").withMethod(Method.DELETE),
                giveEmptyResponse().withStatus(Response.Status.NO_CONTENT.getStatusCode()));

        boolean result = personServiceClient.deletePerson(1);

        assertTrue("Person should be deleted", result);
    }

    @Test
    public void testGetPersonOk() throws JsonProcessingException {
        personDriver.addExpectation(onRequestTo(RESOURCE_URL + "/1").withMethod(Method.GET),
                giveResponse(MAPPER.writeValueAsString(firstPerson), MediaType.APPLICATION_JSON)
                        .withStatus(Response.Status.OK.getStatusCode()));

        Person testPerson = personServiceClient.getPerson(1);

        assertEquals("Persons should be equal", firstPerson, testPerson);
    }
}