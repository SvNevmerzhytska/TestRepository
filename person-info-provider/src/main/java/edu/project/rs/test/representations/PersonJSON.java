package edu.project.rs.test.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.project.rs.test.model.Person;
import edu.project.rs.test.utils.CustomDateSerializer;
import org.joda.time.DateTime;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class PersonJSON {

    public int id;
    @JsonProperty
    public String fullName;
    @JsonProperty
    @JsonSerialize(using = CustomDateSerializer.class)
    public DateTime birthDate;

    public PersonJSON (Person person) {
        this.id = person.id;
        this.fullName = person.firstName + " " + person.lastName;
        this.birthDate = person.birthDate;
    }
}
