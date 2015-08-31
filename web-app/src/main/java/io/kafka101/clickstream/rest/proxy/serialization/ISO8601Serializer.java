package io.kafka101.clickstream.rest.proxy.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.io.IOException;
import java.util.Date;

public class ISO8601Serializer extends JsonSerializer<Date> {

    private static final ISO8601DateFormat ISO_FORMATTER = new ISO8601DateFormat();

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(ISO_FORMATTER.format(value));
    }
}