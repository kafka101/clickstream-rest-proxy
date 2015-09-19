package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Offset {
    public final int partition;
    public final long offset;
    //1 - Non-retriable Kafka exception
    //2 - Retriable Kafka exception; the message might be sent successfully if retried
    public final Long errorCode;
    public final String error;

    @JsonCreator
    public Offset(@JsonProperty("partition") int partition,
            @JsonProperty("offset") long offset,
            @JsonProperty("error_code") long errorCode,
            @JsonProperty("error") String error) {
        this.partition = partition;
        this.offset = offset;
        this.errorCode = errorCode;
        this.error = error;
    }
}
