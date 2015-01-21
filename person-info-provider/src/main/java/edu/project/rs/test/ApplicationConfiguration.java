package edu.project.rs.test;

import io.dropwizard.Configuration;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class ApplicationConfiguration extends Configuration {
    public Class getSpringConfiguration() {
        return MyAppSpringConfiguration.class;
    }
}
