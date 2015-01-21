package edu.project.rs.test.resources;

import edu.project.rs.test.representations.PersonJSON;
import edu.project.rs.test.service.PersonService;
import io.dropwizard.jersey.params.IntParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
@Service
@Path("/person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Autowired
    private PersonService personService;

    @GET
    @Path("/{id}")
    public PersonJSON getPersonById(@PathParam("id")IntParam id) {
        return new PersonJSON(personService.findPerson(id.get()));
    }
}
