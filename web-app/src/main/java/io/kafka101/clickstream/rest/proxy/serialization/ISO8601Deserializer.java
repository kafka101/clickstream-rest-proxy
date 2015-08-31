package io.kafka101.clickstream.rest.proxy.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class ISO8601Deserializer extends JsonDeserializer<Date> {

    private static final ISO8601DateFormat ISO_FORMATTER = new ISO8601DateFormat();

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        return parse(node.asText());
    }

    private Date parse(String date) throws IOException {
        try {
            return ISO_FORMATTER.parse(date);
        } catch (ParseException exception) {
            throw new IOException("Cannot parse date", exception);
        }
    }
}