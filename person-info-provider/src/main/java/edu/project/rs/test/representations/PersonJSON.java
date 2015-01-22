package edu.project.rs.test.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.project.rs.test.model.Person;
import edu.project.rs.test.utils.CustomDateDeserializer;
import edu.project.rs.test.utils.CustomDateSerializer;
import org.joda.time.DateTime;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class PersonJSON {

    public int id;
    @JsonProperty
    public String firstName;
    @JsonProperty
    public String lastName;
    @JsonProperty
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public DateTime birthDate;

    public PersonJSON() {

    }

    public PersonJSON (Person person) {
        this.id = person.id;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.birthDate = person.birthDate;
    }

    public static Person getPerson (PersonJSON personJSON) {
        Person person = new Person(personJSON.firstName, personJSON.lastName, personJSON.birthDate);
        person.id = personJSON.id;
        return person;
    }
}
