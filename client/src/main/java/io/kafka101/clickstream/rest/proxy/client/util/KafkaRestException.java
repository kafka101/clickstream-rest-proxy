package io.kafka101.clickstream.rest.proxy.client.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KafkaRestException extends RuntimeException {

    private Long errorCode;

    @JsonCreator
    public KafkaRestException(@JsonProperty("message") String message, @JsonProperty("error_code") Long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Long getErrorCode() {
        return errorCode;
    }
}