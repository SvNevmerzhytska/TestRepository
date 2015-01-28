package edu.project.rs.test.resources;

import com.wordnik.swagger.annotations.*;
import edu.project.rs.test.model.Person;
import edu.project.rs.test.representations.PersonJSON;
import edu.project.rs.test.service.PersonService;
import io.dropwizard.jersey.params.IntParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
@Service
@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/persons", description = "Operations on persons")
public class PersonResource {

    @Autowired
    private PersonService personService;

    @POST
    @ApiOperation(value = "Create person", response = int.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Person was created"),
            @ApiResponse(code = 400, message = "Invalid JSON"),
            @ApiResponse(code = 422, message = "Entity cannot be processed according to logical constraints"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, constraint violation etc.)")
    })
    public int addPerson(@ApiParam(value = "Person that need to be added to DB", required = true) PersonJSON person) {
        return personService.addPerson(PersonJSON.getPerson(person));
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Find person by defined id", response = PersonJSON.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Given person was found"),
            @ApiResponse(code = 400, message = "Id is not integer"),
            @ApiResponse(code = 404, message = "Person was not found in DB"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, etc.)")
    })
    public PersonJSON getPersonById(@ApiParam(value = "Id of person to find", required = true) @PathParam("id")IntParam id) {
        return new PersonJSON(personService.findPerson(id.get()));
    }

    @GET
    @ApiOperation(value = "List all persons", response = Person.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of persons was successfully retrieved"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, etc.)")
    })
    public List<PersonJSON> getPersons() {
        List<Person> persons = personService.findAllPersons();
        List<PersonJSON> result = new ArrayList<PersonJSON>();
        for (Person person: persons) {
            result.add(new PersonJSON(person));
        }
        return result;
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "Update person with defined id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Persons was successfully updated"),
            @ApiResponse(code = 400, message = "Id is not integer"),
            @ApiResponse(code = 404, message = "Person for update was not found in DB"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, etc.)")
    })
    public void updatePerson(@ApiParam(value = "Id of person to find", required = true) @PathParam("id") IntParam id,
                             PersonJSON personJSON) {
        Person person = PersonJSON.getPerson(personJSON);
        person.id = id.get();

        personService.updatePerson(person);
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Delete person with defined id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Persons was successfully deleted"),
            @ApiResponse(code = 400, message = "Id is not integer"),
            @ApiResponse(code = 404, message = "Person for delete was not found in DB"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, etc.)")
    })
    public void deletePerson(@ApiParam(value = "Id of person to find", required = true) @PathParam("id") IntParam id) {
        personService.deletePerson(id.get());
    }
}
