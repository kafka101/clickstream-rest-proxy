package io.kafka101.clickstream.rest.proxy.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.text.ParseException;

/**
 * Custom class to avoid JSON / Avro fun
 */
public class ISO8601Date {

    private static final ISO8601DateFormat ISO_FORMATTER = new ISO8601DateFormat();

    @JsonProperty
    public final String timestamp;

    @JsonCreator
    public ISO8601Date(String timestamp) {
        this.timestamp = timestamp;
        validate(timestamp);
    }

    private void validate(String timestamp) {
        try {
            ISO_FORMATTER.parse(timestamp);
        } catch (ParseException e) {
            throw new IllegalArgumentException(timestamp + " is not a valid ISO8601 date!");
        }
    }
}
