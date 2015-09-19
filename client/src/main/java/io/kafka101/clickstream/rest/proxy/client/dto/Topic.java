package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Topic {
    public final String name;
    public final int partitions;

    @JsonCreator
    public Topic(@JsonProperty("name") String name, @JsonProperty("partitions") int partitions) {
        this.name = name;
        this.partitions = partitions;
    }
}
