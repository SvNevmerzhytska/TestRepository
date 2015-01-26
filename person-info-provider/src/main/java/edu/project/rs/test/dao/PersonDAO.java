package edu.project.rs.test.dao;

import edu.project.rs.test.model.Person;

import java.util.List;

/**
 * Created by s.nevmerzhytska on 1/19/2015.
 */
public interface PersonDAO {

    int createPerson(Person person);

    Person findPersonById(int id);

    List<Person> findAllPersons();

    void updatePerson(Person person);

    void deletePerson(int id);
}
