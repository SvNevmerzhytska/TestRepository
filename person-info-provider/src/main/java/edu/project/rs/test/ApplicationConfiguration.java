package edu.project.rs.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by s.nevmerzhytska on 1/16/2015.
 */
public class ApplicationConfiguration extends Configuration {

    @NotEmpty
    @JsonProperty
    private String dateFormat;

    private Class springConfiguration = MyAppSpringConfiguration.class;

    public Class getSpringConfiguration() {
        return springConfiguration;
    }

    public void setSpringConfiguration(Class springConfiguration) {
        this.springConfiguration = springConfiguration;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormat.forPattern(dateFormat).withZoneUTC();
    }
}
