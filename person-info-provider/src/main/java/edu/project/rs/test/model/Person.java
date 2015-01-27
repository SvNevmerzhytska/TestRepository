package edu.project.rs.test.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Column(name = "first_name")
    @NotNull
    @Size(min = 3, max = 255)
    public String firstName;

    @Column(name = "last_name")
    @NotNull
    @Size(min = 3, max = 255)
    public String lastName;

    @Column(name = "birth_date")
    @Past
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime birthDate;

    public Person() {

    }

    public Person (String firstName, String lastName, DateTime birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Person)) return false;

        final Person that = (Person) object;

        return Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.birthDate.getDayOfYear(), that.birthDate.getDayOfYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate);
    }

    @Override
    public String toString() {
        return "id=" + id + ", " + firstName + " " + lastName + ", " + birthDate.year().getAsText();
    }
}
