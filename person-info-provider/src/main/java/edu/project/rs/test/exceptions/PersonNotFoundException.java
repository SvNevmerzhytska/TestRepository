package edu.project.rs.test.exceptions;

import com.sun.jersey.api.Responses;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by s.nevmerzhytska on 1/21/2015.
 * Indicate that there is no appropriate row in DB
 */
public class PersonNotFoundException extends WebApplicationException {
    public PersonNotFoundException() {
        super(Responses.notFound().build());
    }

    public PersonNotFoundException(String message) {
        super(Response.status(Responses.NOT_FOUND).
                entity(message).type("text/plain").build());
    }
}
