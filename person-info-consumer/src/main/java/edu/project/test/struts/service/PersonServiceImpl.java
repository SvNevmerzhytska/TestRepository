package edu.project.test.struts.service;

import edu.project.test.struts.model.Person;
import edu.project.test.struts.webclient.PersonServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by s.nevmerzhytska on 2/18/2015.
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonServiceClient personServiceClient;

    @Override
    public List<Person> getAllPersons() {
        return personServiceClient.getPersonList();
    }

    @Override
    public Person getPersonById(int personId) {
        return personServiceClient.getPerson(personId);
    }

    @Override
    public void insertPerson(Person person) {
        person.id = personServiceClient.postPerson(person);
    }

    @Override
    public void updatePerson(Person person) {
        personServiceClient.updatePerson(person);
    }

    @Override
    public void deletePerson(int personId) {
        personServiceClient.deletePerson(personId);
    }
}
