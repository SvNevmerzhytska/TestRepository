package edu.project.test.struts.webclient;

import edu.project.test.struts.exceptions.ExternalServerError;
import edu.project.test.struts.exceptions.InvalidRequestException;
import edu.project.test.struts.model.Person;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by s.nevmerzhytska on 2/5/2015.
 */
@Component
public class PersonServiceClient {

    @Autowired
    @Resource
    WebClient personsWebClient;

    public int postPerson(Person person) {
        Response response = personsWebClient.post(person);
        if (response.getStatus() >= 500) {
            throw new ExternalServerError();
        }
        if (response.getStatus() != 200) {
            throw new InvalidRequestException(Integer.toString(response.getStatus()));
        }
        return response.readEntity(Integer.class);
    }

    public List<Person> getPersonList() {
        Response response = personsWebClient.get();
        if (response.getStatus() >= 500) {
            throw new ExternalServerError();
        }
        return response.readEntity(new GenericType<List<Person>>() {
        });
    }

    public boolean updatePerson(Person person) {
        Response response = personsWebClient.path(person.id).put(person);
        if (response.getStatus() >= 500) {
            throw new ExternalServerError();
        }
        if (response.getStatus() != 204) {
            throw new InvalidRequestException(Integer.toString(response.getStatus()));
        }
        return true;
    }

    public boolean deletePerson(int id) {
        Response response = personsWebClient.path(id).delete();
        if (response.getStatus() >= 500) {
            throw new ExternalServerError();
        }
        if (response.getStatus() != 204) {
            throw new InvalidRequestException(Integer.toString(response.getStatus()));
        }
        return true;
    }

    public Person getPerson(int id) {
        Response response = personsWebClient.path(id).get();
        if (response.getStatus() >= 500) {
            throw new ExternalServerError();
        }
        if (response.getStatus() != 200) {
            throw new InvalidRequestException(Integer.toString(response.getStatus()));
        }
        return response.readEntity(Person.class);
    }
}
