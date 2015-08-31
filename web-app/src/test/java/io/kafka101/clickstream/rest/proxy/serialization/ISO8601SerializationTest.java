package io.kafka101.clickstream.rest.proxy.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Test;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ISO8601SerializationTest {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new SimpleModule()
                            .addDeserializer(Date.class, new ISO8601Deserializer())
                            .addSerializer(Date.class, new ISO8601Serializer())
            );

    @Test
    public void dateSerialization() throws IOException {
        long dateWithoutMillis = Math.abs(new Date().getTime() / 1000) * 1000;
        Date date = new Date(dateWithoutMillis);

        String json = mapper.writeValueAsString(date);
        assertThat(mapper.readValue(json, Date.class), is(equalTo(date)));
    }

    @Test
    public void readsISO8601Date() throws IOException {
        String date = "\"2015-01-01T12:34:56Z\"";

        Calendar calendar = new Calendar.Builder()
                .setTimeZone(TimeZone.getTimeZone(ZoneId.of("Z")))
                .setDate(2015, Calendar.JANUARY, 1)
                .setTimeOfDay(12, 34, 56, 0)
                .build();

        assertThat(mapper.readValue(date, Date.class), is(equalTo(calendar.getTime())));
    }

}
