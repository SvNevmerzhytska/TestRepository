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

    public Class getSpringConfiguration() {
        return MyAppSpringConfiguration.class;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormat.forPattern(dateFormat);
    }
}
