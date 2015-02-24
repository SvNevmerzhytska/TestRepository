package edu.project.rs.test.service;

import edu.project.rs.test.dao.PersonDAO;
import edu.project.rs.test.exceptions.NotFoundException;
import edu.project.rs.test.model.Person;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by s.nevmerzhytska on 1/20/2015.
 */
public class PersonServiceTest {

    private List<Person> mockPersons = new ArrayList<Person>();
    private Person firstPerson;

    private PersonDAO personDAO = mock(PersonDAO.class);

    private static int INITIAL_SIZE = 2;

    @InjectMocks
    private PersonService personService = new PersonServiceImpl();

    @Before
    public void setUp() {
        firstPerson = new Person("Kate", "First", DateTime.now().minusYears(20));
        firstPerson.id = 1;

        mockPersons.add(firstPerson);
        Person secondPerson = new Person("Simon", "Second", DateTime.now().minusYears(25));
        secondPerson.id = 2;
        mockPersons.add(secondPerson);


        when(personDAO.findAllPersons()).thenReturn(mockPersons);

        when(personDAO.findPersonById(1)).thenReturn(mockPersons.get(0));
        when(personDAO.findPersonById(10)).thenReturn(null);

        when(personDAO.createPerson(any(Person.class))).thenAnswer(new Answer<Integer>() {
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
        }).when(personDAO).updatePerson(any(Person.class));

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
        }).when(personDAO).deletePerson(anyInt());

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void cleanUp(){
        mockPersons.clear();
    }

    @Test
    public void testFindAllPersons() {
        List<Person> persons = personService.findAllPersons();

        assertEquals("Number of mockPersons should be equal to number of inserted in setUp method", INITIAL_SIZE, persons.size());
        assertTrue("Database should contain specified person", persons.contains(firstPerson));
    }

    @Test
    public void testAddPerson() {
        Person testPerson = new Person("TestName", "TestSurname", DateTime.now().minusYears(30));

        int personId = personService.addPerson(testPerson);

        List<Person> personList = personService.findAllPersons();

        assertTrue("Created person should have id", personId != 0);
        assertEquals("Number of persons should be greater on one than initial", INITIAL_SIZE + 1, personList.size());
        assertTrue("Created person should be in the list of all persons" + testPerson, personList.contains(testPerson));
    }

    @Test
    public void testFindPersonByExsistentId() {
        Person expectedPerson = new Person("Kate", "First", DateTime.now().minusYears(20));

        Person person = personService.findPerson(1);

        assertEquals("Person should be equal to first inserted person in setUp method", expectedPerson, person);
    }

    @Test(expected = NotFoundException.class)
    public void testFindPersonByNotExsistentId() {
        Person person = personService.findPerson(10);

        assertNull("Database should not contain person with id 10", person);
    }

    @Test
    public void testUpdateExsistentPerson() {
        Person person = new Person("BrandNewName", "BrandNewSurname", DateTime.now());
        person.id = 1;

        personService.updatePerson(person);

        List<Person> persons = personService.findAllPersons();

        assertEquals("Number of rows in list of persons should remain the same", INITIAL_SIZE, persons.size());
        assertTrue("List of persons should contain updated person", persons.contains(person));
        assertFalse("Person data should be changed", persons.contains(firstPerson));
    }

    @Test
    public void testUpdateNonExsistentPerson() {
        Person person = new Person("BrandNewName", "BrandNewSurname", DateTime.now());

        personService.updatePerson(person);

        List<Person> persons = personService.findAllPersons();

        assertFalse("List of persons should not contain new person", persons.contains(person));
        assertEquals("Number of rows in list of persons should remain the same", INITIAL_SIZE, persons.size());
    }

    @Test
    public void testDeleteExistentPerson() {
        Person person = new Person("Kate", "First", DateTime.now().minusYears(20));
        person.id = 1;

        personService.deletePerson(1);

        List<Person> persons = personService.findAllPersons();

        assertFalse("Database should not contain deleted person", persons.contains(person));
        assertEquals("Number of rows in DB should be decresed by 1", INITIAL_SIZE - 1, persons.size());
    }

    @Test
    public void testDeleteNonExistentPerson() {
        personService.deletePerson(10);

        List<Person> persons = personService.findAllPersons();

        assertEquals("Number of rows in DB should remain the same", INITIAL_SIZE, persons.size());
    }
}
