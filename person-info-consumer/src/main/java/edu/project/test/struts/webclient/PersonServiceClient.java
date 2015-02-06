package edu.project.test.struts.webclient;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s.nevmerzhytska on 2/5/2015.
 */
public class PersonServiceClient {

    public static void main(String[] args) {
        List<Object> providers = new ArrayList<Object>();
        providers.add(new JacksonJaxbJsonProvider());

        WebClient client = WebClient.create("http://localhost:8080/persons", providers);
        client.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).path("/1");

        Response response = client.get();
        System.out.println("status = " + response.getStatus() + ", person id = " + response.readEntity(String.class));
    }
}
