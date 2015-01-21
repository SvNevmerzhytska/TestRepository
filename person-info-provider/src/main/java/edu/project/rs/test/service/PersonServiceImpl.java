package edu.project.rs.test.service;

import edu.project.rs.test.dao.PersonDAO;
import edu.project.rs.test.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by s.nevmerzhytska on 1/20/2015.
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonDAO personDAO;

    @Override
    public Person findPerson(int id) {
        return personDAO.findPersonById(id);
    }
}
