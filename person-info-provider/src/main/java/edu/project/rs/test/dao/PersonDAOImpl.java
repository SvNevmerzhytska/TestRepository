package edu.project.rs.test.dao;

import edu.project.rs.test.model.Person;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by s.nevmerzhytska on 1/19/2015.
 */
@Repository("personDAO")
@Transactional
public class PersonDAOImpl implements PersonDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     *
     * @param person
     * @return id of created person in DB
     */
    @Override
    public int createPerson(Person person) {
        sessionFactory.getCurrentSession().persist(person);
        sessionFactory.getCurrentSession().flush();
        return person.id;
    }

    /**
     *
     * @param id
     * @return {@link edu.project.rs.test.model.Person} object if it exists in DB
     * OR null if it is not
     */
    @Override
    public Person findPersonById(int id) {
        return (Person) sessionFactory.getCurrentSession().get(Person.class, id);
    }

    /**
     *
     * @return list of {@link edu.project.rs.test.model.Person}s that exist in DB
     * (if table is empty return zero sized list
     */
    @Override
    public List<Person> findAllPersons() {
        return sessionFactory.getCurrentSession().createCriteria(Person.class).list();
    }

    /**
     * Updates only exsistent person in DB
     * @param person
     */
    @Override
    public void updatePerson(Person person) {
        if(findPersonById(person.id) != null) {
            sessionFactory.getCurrentSession().merge(person);
        }
    }

    /**
     * Delete only existent person in DB
     * @param id
     */
    @Override
    public void deletePerson(int id) {
        Person person = (Person) sessionFactory.getCurrentSession().get(Person.class, id);
        if (person != null) {
            sessionFactory.getCurrentSession().delete(person);
        }
    }

}
