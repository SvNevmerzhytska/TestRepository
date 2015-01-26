package edu.project.rs.test.service;

import edu.project.rs.test.dao.PersonDAO;
import edu.project.rs.test.exceptions.PersonNotFoundException;
import edu.project.rs.test.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by s.nevmerzhytska on 1/20/2015.
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonDAO personDAO;

    @Override
    @Transactional
    public int addPerson(Person person) {
        return personDAO.createPerson(person);
    }

    @Override
    public Person findPerson(int id) {
        Person person = personDAO.findPersonById(id);
        if (person == null) {
            throw new PersonNotFoundException();
        }
        return person;
    }

    @Override
    public List<Person> findAllPersons() {
        return personDAO.findAllPersons();
    }

    @Override
    @Transactional
    public void updatePerson(Person person) {
        personDAO.updatePerson(person);
    }

    @Override
    @Transactional
    public void deletePerson(int id) {
        personDAO.deletePerson(id);
    }
}
