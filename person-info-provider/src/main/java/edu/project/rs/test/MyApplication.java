package edu.project.rs.test;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import edu.project.rs.test.utils.CustomDateDeserializer;
import edu.project.rs.test.utils.CustomDateSerializer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.joda.time.DateTime;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.EnumSet;
import java.util.Map;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class MyApplication extends Application<ApplicationConfiguration> {

    private Class springConfigurationClass = MyAppSpringConfiguration.class;

    public static void main(String[] args) throws Exception {
        new MyApplication().run(new String[] { "server", "dropwizard-config.yml" });
    }

    @Override
    public void initialize(Bootstrap bootstrap) {

    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws Exception {

        initSpringContextAndRegisterResources(applicationConfiguration, environment);

        configureSwagger(environment);

        allowCORS(environment);

        configureSerializersDeserializers(applicationConfiguration, environment);
    }

    protected void initSpringContextAndRegisterResources(ApplicationConfiguration applicationConfiguration, Environment environment) {

        // Init Spring context before we init the app context, we have to create a parent context with all the
        // config objects others rely on to get initialized
        AnnotationConfigWebApplicationContext parent = new AnnotationConfigWebApplicationContext();
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

        parent.refresh();
        parent.getBeanFactory().registerSingleton("configuration", applicationConfiguration);
        parent.registerShutdownHook();
        parent.start();

        //the real main app context has a link to the parent context
        ctx.setParent(parent);
        ctx.register(applicationConfiguration.getSpringConfiguration());
        ctx.refresh();
        ctx.registerShutdownHook();
        ctx.start();

        //now that Spring is started, let's get all the beans that matter into DropWizard

        //health checks
        Map<String, HealthCheck> healthChecks = ctx.getBeansOfType(HealthCheck.class);
        for(Map.Entry<String,HealthCheck> entry : healthChecks.entrySet()) {
            environment.healthChecks().register("template", entry.getValue());
        }

        //resources
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        for(Map.Entry<String,Object> entry : resources.entrySet()) {
            environment.jersey().register(entry.getValue());
        }

        //exception mappers
        Map<String, Object> exceptionMappers = ctx.getBeansWithAnnotation(Provider.class);
        for(Map.Entry<String, Object> entry: exceptionMappers.entrySet()) {
            environment.jersey().register(entry.getValue());
        }
    }

    private void configureSwagger(Environment environment) {

        //Configure Swagger
        environment.jersey().register(new ApiListingResourceJSON());
        environment.jersey().register(new ResourceListingProvider());
        environment.jersey().register(new ApiDeclarationProvider());
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());
    }

    private void allowCORS(Environment environment) {

        // Enable CORS headers
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private void configureSerializersDeserializers(ApplicationConfiguration applicationConfiguration, Environment environment) {
        //for joda-datetime serialization/deserialization
        ObjectMapper objectMapper = environment.getObjectMapper();
        JodaModule jodaModule = new JodaModule();
        jodaModule.addDeserializer(DateTime.class, new CustomDateDeserializer(applicationConfiguration.getDateTimeFormatter()));
        jodaModule.addSerializer(DateTime.class, new CustomDateSerializer(applicationConfiguration.getDateTimeFormatter()));
        objectMapper.registerModule(jodaModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
