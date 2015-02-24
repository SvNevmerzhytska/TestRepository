package edu.project.test.struts.exceptions;

/**
 * Created by s.nevmerzhytska on 2/10/2015.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
