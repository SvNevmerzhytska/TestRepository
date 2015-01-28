package edu.project.rs.test;

import edu.project.rs.test.resources.PersonResource;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class MyApplicationTest {
    private final Environment environment = mock(Environment.class);
    private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);
    private final MyApplication application = new MyApplication();
    private final ApplicationConfiguration configuration = new TestApplicationConfiguration();

    @Before
    public void setup() {
        when(environment.jersey()).thenReturn(jersey);
    }

    @Test
    public void testBuildResouces() throws Exception {
        application.initSpringContextAndRegisterResources(configuration, environment);
        verify(jersey).register(isA(PersonResource.class));
    }
}
