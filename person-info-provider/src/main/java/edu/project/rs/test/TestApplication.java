package edu.project.rs.test;

import edu.project.rs.test.resources.PersonResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class TestApplication extends Application<ApplicationConfiguration> {
    public static void main(String[] args) throws Exception {
        new TestApplication().run(new String[] { "server" });
    }

    @Override
    public void initialize(Bootstrap bootstrap) {

    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new PersonResource());
    }
}
