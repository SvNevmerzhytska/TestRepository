package edu.project.test.struts.service;

import edu.project.test.struts.model.Person;

import java.util.List;

/**
 * Created by s.nevmerzhytska on 2/18/2015.
 */
public interface PersonService {

    List<Person> getAllPersons();

    Person getPersonById(int personId);

    void insertPerson(Person person);

    void updatePerson(Person person);

    void deletePerson(int personId);
}
