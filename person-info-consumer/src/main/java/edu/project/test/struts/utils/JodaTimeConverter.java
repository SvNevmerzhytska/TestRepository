package edu.project.test.struts.utils;

import org.apache.struts2.util.StrutsTypeConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Map;

/**
 * Created by s.nevmerzhytska on 2/23/2015.
 */
public class JodaTimeConverter extends StrutsTypeConverter {
    /**
     * convert DateTime to string
     *
     * @param map      map
     * @param dateTime DateTime object
     * @return string
     */
    public String convertToString(Map map, Object dateTime) {
        if (dateTime instanceof DateTime) {
            DateTime time = (DateTime) dateTime;
            if (time.getHourOfDay() + time.getMinuteOfHour() + time.getSecondOfMinute() == 0) {
                return time.toString("yyyy-MM-dd");
            } else {
                return time.toString("yyyy-MM-dd HH:mm:ss");
            }
        }
        return null;
    }

    /**
     * convert string to DateTime object
     *
     * @param map    map
     * @param values values
     * @param clazz  class
     * @return DateTime object
     */
    public Object convertFromString(Map map, String[] values, Class clazz) {
        if (values != null && values.length > 0) {
            return parseValue(values[0]);
        }
        return null;
    }

    /**
     * parse date string
     *
     * @param value date string
     * @return DateTime object
     */
    public DateTime parseValue(String value) {
        value = value.trim();
        try {
            if (!value.contains(" ")) //time contained
            {
                return DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC().parseDateTime(value);
            } else //date only
            {
                return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC().parseDateTime(value);
            }
        } catch (Exception e) {
            return null;
        }
    }
}
