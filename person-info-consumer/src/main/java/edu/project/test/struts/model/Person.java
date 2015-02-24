package edu.project.test.struts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.Objects;

/**
 * Created by s.nevmerzhytska on 2/5/2015.
 */
public class Person {

    public int id;
    @JsonProperty(required = true)
    public String firstName;
    @JsonProperty(required = true)
    public String lastName;
    @JsonProperty
    public DateTime birthDate;

    public Person() {
    }

    public Person(String firstName, String lastName, DateTime birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Person(int id, String firstName, String lastName, DateTime birthDate) {
        this(firstName, lastName, birthDate);
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Person)) return false;

        final Person that = (Person) obj;

        return Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.birthDate.getDayOfYear(), that.birthDate.getDayOfYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "Person [id=" + this.id + ", " + this.firstName + " " + this.lastName + ", " + this.birthDate.year().getAsText() + "]";
    }
}
