package edu.project.rs.test.dao;

import edu.project.rs.test.model.Person;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by s.nevmerzhytska on 1/19/2015.
 */
@Repository("personDAO")
public class PersonDAOImpl implements PersonDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int createPerson(Person person) {
        sessionFactory.getCurrentSession().persist(person);
        sessionFactory.getCurrentSession().flush();
        return person.id;
    }

    @Override
    public Person findPersonById(int id) {
        return new Person("Ann", "Black", DateTime.now().minusYears(20));
    }

    @Override
    public List<Person> findAllPersons() {
        return sessionFactory.getCurrentSession().createCriteria(Person.class).list();
    }

    @Override
    public void updatePerson(Person person) {

    }

    @Override
    public void deletePerson(Person person) {

    }

    @Override
    public void deletePersonById(int id) {

    }
}
