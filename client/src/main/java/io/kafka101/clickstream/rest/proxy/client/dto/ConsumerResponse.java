package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public class ConsumerResponse {
    public final String instanceId;
    public final URI baseUri;

    public ConsumerResponse(@JsonProperty("instance_id") String instanceId, @JsonProperty("base_uri") URI baseUri) {
        this.instanceId = instanceId;
        this.baseUri = baseUri;
    }
}
