package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsumerRecord<K, V> extends Record<K, V> {
    public final int partition;
    public final long offset;

    @JsonCreator
    public ConsumerRecord(@JsonProperty("partition") int partition,
            @JsonProperty("offset") long offset,
            @JsonProperty("key") K key,
            @JsonProperty("value") V value) {
        super(value, key);
        this.partition = partition;
        this.offset = offset;
    }
}
