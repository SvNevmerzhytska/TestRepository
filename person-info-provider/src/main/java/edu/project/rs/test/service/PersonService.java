package edu.project.rs.test.service;

import edu.project.rs.test.model.Person;

import java.util.List;

/**
 * Created by s.nevmerzhytska on 1/20/2015.
 */
public interface PersonService {

    int addPerson(Person person);

    Person findPerson(int id);

    List<Person> findAllPersons();

    void updatePerson(Person person);

    void deletePerson(Person person);
}
