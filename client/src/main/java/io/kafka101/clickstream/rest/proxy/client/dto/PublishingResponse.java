package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class PublishingResponse {
    public final List<Offset> offsets;
    public final Integer keySchemaId;
    public final Integer valueSchemaId;

    @JsonCreator
    public PublishingResponse(@JsonProperty("offsets") List<Offset> offsets,
            @JsonProperty("key_schema_id") Integer keySchemaId,
            @JsonProperty("value_schema_id") Integer valueSchemaId) {
        this.offsets = offsets;
        this.keySchemaId = keySchemaId;
        this.valueSchemaId = valueSchemaId;
    }
}
