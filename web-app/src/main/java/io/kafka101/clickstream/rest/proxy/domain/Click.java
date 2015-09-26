package io.kafka101.clickstream.rest.proxy.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class Click {

    public final ISO8601Date timestamp;
    public final Coordinate coordinate;
    public final String destination;
    public final URL referrer, source;

    private String userAgent;
    private URL ip;

    @JsonCreator
    public Click(@JsonProperty("timestamp") ISO8601Date timestamp,
            @JsonProperty("coords") Coordinate coordinate,
            @JsonProperty("destination") String destination,
            @JsonProperty("referrer") URL referrer,
            @JsonProperty("source") URL source) {
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
