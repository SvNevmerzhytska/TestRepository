package edu.project.rs.test;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class TestApplicationConfiguration extends ApplicationConfiguration {
    public Class getSpringConfiguration() {
        return MyTestAppSpringConfiguration.class;
    }
}
