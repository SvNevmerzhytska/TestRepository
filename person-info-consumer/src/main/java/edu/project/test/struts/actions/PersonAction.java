package edu.project.test.struts.actions;

import com.opensymphony.xwork2.ActionSupport;
import edu.project.test.struts.model.Person;
import edu.project.test.struts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by s.nevmerzhytska on 2/13/2015.
 */
public class PersonAction extends ActionSupport {

    @Autowired
    private PersonService personService;

    private Person person;
    private List<Person> persons;

    public String getAllPersons() {
        persons = personService.getAllPersons();
        return SUCCESS;
    }

    public String setUpForInsertOrUpdate() {
        if (person != null && person.id != 0) {
            person = personService.getPersonById(person.id);
        }
        return SUCCESS;
    }

    public String insertOrUpdate() {
        if (!isPersonValid()) {
            return INPUT;
        } else {
            if (person.id == 0) {
                personService.insertPerson(person);
            } else {
                personService.updatePerson(person);
            }
        }
        return SUCCESS;
    }

    public String deletePerson() {
        personService.deletePerson(person.id);
        addActionMessage("Person was deleted");
        return SUCCESS;
    }

    private boolean isPersonValid() {
        if (person.firstName == null || person.firstName.trim().equals("")) {
            addActionMessage("First Name is required");
        } else {
            if (person.firstName.trim().length() < 3) {
                addActionMessage("First Name should be at least 3 character length");
            }
        }

        if (person.lastName == null || person.lastName.trim().equals("")) {
            addActionMessage("Last Name is required");
        } else {
            if (person.lastName.trim().length() < 3) {
                addActionMessage("Last Name should be at least 3 character length");
            }
        }

        /* TODO: validation for date*/

        if (this.hasActionMessages()) {
            return false;
        } else {
            return true;
        }
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
