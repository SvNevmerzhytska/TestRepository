package edu.project.rs.test.dao;

import edu.project.rs.test.MyTestAppSpringConfiguration;
import edu.project.rs.test.model.Person;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by s.nevmerzhytska on 1/20/2015.
 */
@ContextConfiguration(classes = {MyTestAppSpringConfiguration.class})
public class personDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private PersonDAO personDAO;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");

    @Before
    public void setUp() {
        executeSqlScript("insert-test-data.sql", false);
    }

    @After
    public void cleanUp() {
        executeSqlScript("delete-test-data.sql", false);
    }

    @Test
    public void testFindAll() {
        // according to insert-test-data.sql
        List<Person> expectedPersons = new ArrayList<Person>();
        expectedPersons.add(new Person("Ann", "Test1", DateTime.parse("17-09-1990", formatter)));
        expectedPersons.add(new Person("Kate", "Test2", DateTime.parse("01-01-1991", formatter)));
        expectedPersons.add(new Person("Tom", "Test3", DateTime.parse("31-08-1989", formatter)));

        List<Person> persons = personDAO.findAllPersons();

        assertEquals("Number of persons in database should be equal to number of inserted in script rows", 3, persons.size());
        assertTrue("Database should contain all test persons", persons.containsAll(expectedPersons));
    }

    @Test
    public void testFindAllEmptyDB() {
        cleanUp();

        List<Person> persons = personDAO.findAllPersons();

        assertNotNull("DAO should be a List object", persons);
        assertEquals("Number of rows in DB should be 0", 0, persons.size());
    }

    @Test
    public void testCreatePersonValid() {
        Person testPerson = new Person("TestName", "TestSurname", DateTime.now().minusYears(30));
        int initCount = personDAO.findAllPersons().size();

        int personId = personDAO.createPerson(testPerson);

        List<Person> personList = personDAO.findAllPersons();
        int countAfterInsertion = personList.size();

        assertTrue("Database should generate id for person", personId != 0);
        assertEquals("Number of persons should be (greater on one than initial)", initCount + 1, countAfterInsertion);
        assertTrue("Database should contain person " + testPerson, personList.contains(testPerson));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCreatePersonWithoutMandatoryName() {
        Person testPerson = new Person(null, "TestSurname", DateTime.now().minusYears(30));

        int personId = personDAO.createPerson(testPerson);
    }

    @Test
    public void testFindPersonByExsistentId() {
        Person expectedPerson = new Person("Ann", "Test1", DateTime.parse("17-09-1990", formatter));

        Person person = personDAO.findPersonById(1);

        assertEquals("Person should be equal to first inserted person into DB", expectedPerson, person);
    }

    @Test
    public void testFindPersonByNotExsistentId() {
        Person person = personDAO.findPersonById(10);

        assertNull("Database should not contain person with id 10", person);
    }

    @Test
    public void testUpdateExsistentPerson() {
        Person person = new Person("BrandNewName", "BrandNewSurname", DateTime.now());
        person.id = 1;

        personDAO.updatePerson(person);

        Person person2 = personDAO.findPersonById(1);

        assertEquals("Name of person should be updated", "BrandNewName", person2.firstName);
        assertEquals("Id of person should not be changed", person.id, person2.id);
    }

    @Test
    public void testUpdateNonExsistentPerson() {
        Person person = new Person("BrandNewName", "BrandNewSurname", DateTime.now());

        personDAO.updatePerson(person);

        List<Person> persons = personDAO.findAllPersons();

        assertFalse("Database should not contain new person", persons.contains(person));
        assertEquals("Number of rows in DB should remain the same", 3, persons.size());
    }

    @Test
    public void testDeleteExistentPerson() {
        Person person = new Person("Ann", "Test1", DateTime.parse("17-09-1990", formatter));

        personDAO.deletePerson(1);

        List<Person> persons = personDAO.findAllPersons();

        assertFalse("Database should not contain deleted person", persons.contains(person));
        assertEquals("Number of rows in DB should be decresed by 1", 2, persons.size());
    }

    @Test
    public void testDeleteNonExistentPerson() {
        personDAO.deletePerson(10);

        List<Person> persons = personDAO.findAllPersons();

        assertEquals("Number of rows in DB should remain the same", 3, persons.size());
    }
}
