package io.kafka101.clickstream.rest.proxy.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.kafka101.clickstream.rest.proxy.serialization.ISO8601Deserializer;
import io.kafka101.clickstream.rest.proxy.serialization.ISO8601Serializer;

import java.net.URL;
import java.util.Date;

public class Click {

    @JsonDeserialize(using = ISO8601Deserializer.class)
    @JsonSerialize(using = ISO8601Serializer.class)
    public final Date timestamp; //ISO DATE
    public final Coordinate<Integer> coordinate;
    public final String destination;
    public final URL referrer, source;

    private String userAgent;
    private URL ip;

    @JsonCreator
    public Click(@JsonProperty("timestamp") Date timestamp,
            @JsonProperty("coords") Coordinate<Integer> coordinate,
            @JsonProperty("destination") String destination,
            @JsonProperty("referrer")URL referrer,
            @JsonProperty("source")URL source) {
        this.timestamp = timestamp;
        this.coordinate = coordinate;
        this.destination = destination;
        this.referrer = referrer;
        this.source = source;
    }

    public URL getIp() {
        return ip;
    }

    public void setIp(URL ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
