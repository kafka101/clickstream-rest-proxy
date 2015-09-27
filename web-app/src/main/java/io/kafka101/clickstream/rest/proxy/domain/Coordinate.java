package io.kafka101.clickstream.rest.proxy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {
    public final int x, y;

    public Coordinate(@JsonProperty("x")int x, @JsonProperty("y")int y) {
        this.x = x;
        this.y = y;
    }
}
