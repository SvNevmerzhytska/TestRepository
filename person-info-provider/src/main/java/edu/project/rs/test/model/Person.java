package edu.project.rs.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class Person {
    public int id;
    @JsonProperty
    public String firstName;
    @JsonProperty
    public String lastName;
    @JsonProperty
    public Calendar birthDate;

    public Person (int id, String firstName, String lastName, Calendar birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }
}
