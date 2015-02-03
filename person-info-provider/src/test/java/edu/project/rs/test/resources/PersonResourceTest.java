package edu.project.rs.test.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import edu.project.rs.test.exceptions.NotFoundException;
import edu.project.rs.test.model.Person;
import edu.project.rs.test.representations.PersonJSON;
import edu.project.rs.test.service.PersonService;
import edu.project.rs.test.service.PersonServiceImpl;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by s.nevmerzhytska on 1/20/2015.
 */
public class PersonResourceTest {
    private static List<Person> mockPersons = new ArrayList<Person>();
    private static Person firstPerson;
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy").withZoneUTC();
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final PersonService personService = mock(PersonServiceImpl.class);

    private static int INITIAL_SIZE = 3;
    private static String RESOURCE_URL = "/persons";

    @InjectMocks
    private static PersonResource personResource = new PersonResource();

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(personResource).build();

    @Before
    public void setUp() {
        firstPerson = new Person("Ann", "Test1", DateTime.parse("17-09-1990", FORMATTER));
        firstPerson.id = 1;
        mockPersons.add(firstPerson);

        Person secondPerson = new Person("Kate", "Test2", DateTime.parse("01-01-1991", FORMATTER));
        secondPerson.id = 2;
        mockPersons.add(secondPerson);

        Person thirdPerson = new Person("Tom", "Test3", DateTime.parse("31-08-1989", FORMATTER));
        thirdPerson.id = 3;
        mockPersons.add(thirdPerson);

        when(personService.findAllPersons()).thenReturn(mockPersons);

        when(personService.findPerson(1)).thenReturn(mockPersons.get(0));
        when(personService.findPerson(10)).thenThrow(new NotFoundException(NotFoundException.PERSON_NOT_FOUND));

        when(personService.addPerson(any(Person.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                Person person = (Person) invocationOnMock.getArguments()[0];
                person.id = mockPersons.size() + 1;
                mockPersons.add(person);
                return person.id;
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Person person = (Person) invocationOnMock.getArguments()[0];
                for(Person p: mockPersons) {
                    if(p.id == person.id) {
                        mockPersons.set(p.id - 1, person);
                    }
                }
                return null;
            }
        }).when(personService).updatePerson(any(Person.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                int id = (int) invocationOnMock.getArguments()[0];
                if (id < mockPersons.size()) {
                    Person person = mockPersons.get(id - 1);
                    mockPersons.remove(person);
                }
                return null;
            }
        }).when(personService).deletePerson(anyInt());

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void cleanUp(){
        mockPersons.clear();
        reset(personService);
    }

    @Test
    public void testAddPersonOk() throws JsonProcessingException {
        PersonJSON testPerson = new PersonJSON(new Person("Jane", "Test", DateTime.parse("31-01-1990", FORMATTER)));

        assertThat(resources.client().resource(RESOURCE_URL).header("Content-Type", "application/json")
                .post(Integer.class, MAPPER.writeValueAsString(testPerson)))
                .isGreaterThan(INITIAL_SIZE);
        assertTrue("Specified person should be added to list", mockPersons.contains(PersonJSON.getPerson(testPerson)));
    }

    @Test
    public void testGetPersons() {
        List<PersonJSON> personJSONs = new ArrayList<PersonJSON>();
        for(Person person: mockPersons) {
            personJSONs.add(new PersonJSON(person));
        }

        assertThat(resources.client().resource(RESOURCE_URL)
                .get(new GenericType<List<PersonJSON>>() {
                }).containsAll(personJSONs));
    }

    @Test
    public void testGetPersonByIdOk() {
        assertThat(resources.client().resource(RESOURCE_URL + "/1").get(PersonJSON.class))
                .isEqualTo(new PersonJSON(firstPerson));
    }

    @Test
    public void testGetPersonByInvalidId() {
        ClientResponse response = resources.client().resource(RESOURCE_URL + "/invalid")
                .get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testUpdatePersonInvalidId() {
        PersonJSON testPerson = new PersonJSON(new Person("Jane", "Test", DateTime.parse("31-01-1990", FORMATTER)));

        ClientResponse response = resources.client().resource(RESOURCE_URL + "/invalid")
                .header("Content-Type", "application/json")
                .put(ClientResponse.class, testPerson);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testUpdatePersonOk() {
        PersonJSON testPerson = new PersonJSON(new Person("Jane", "Test", DateTime.parse("31-01-1990", FORMATTER)));

        resources.client().resource(RESOURCE_URL + "/1")
                .header("Content-Type", "application/json")
                .put(testPerson);
        assertTrue("Person list should contain updated person", mockPersons.contains(PersonJSON.getPerson(testPerson)));
        assertFalse("Person list should not contain old person", mockPersons.contains(firstPerson));
    }

    @Test
    public void testDeletePerson() {
        resources.client().resource(RESOURCE_URL + "/1").delete();
        assertFalse("First person should be deleted from person list", mockPersons.contains(firstPerson));
        assertEquals("Person list should be decresed by 1", INITIAL_SIZE - 1, mockPersons.size());
    }
}
