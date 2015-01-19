package edu.project.rs.test.resources;

import edu.project.rs.test.model.Person;
import io.dropwizard.jersey.params.IntParam;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Calendar;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
@Service
@Path("/person/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @GET
    public Person getPerson(@PathParam("id")IntParam id) {
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(Calendar.YEAR, 1990);
        return new Person(id.get(), "Jhon", "Doe", birthDate);
    }
}
