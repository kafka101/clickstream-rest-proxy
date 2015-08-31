package io.kafka101.clickstream.rest.proxy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate<T> {
    public final T x, y;

    public Coordinate(@JsonProperty("x")T x, @JsonProperty("y")T y) {
        this.x = x;
        this.y = y;
    }
}
