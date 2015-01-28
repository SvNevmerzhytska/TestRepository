package edu.project.rs.test.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by s.nevmerzhytska on 1/27/2015.
 */
@Provider
@Service
public class HibernateConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response.status(422).type("text/plain").entity(e.getCause().getMessage()).build();
    }
}
