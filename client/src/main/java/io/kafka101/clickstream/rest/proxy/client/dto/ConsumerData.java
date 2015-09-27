package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.kafka101.clickstream.rest.proxy.client.util.EnumSerializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumerData {

    @JsonSerialize(using = EnumSerializer.class)
    public enum Format {AVRO, BINARY};
    @JsonSerialize(using = EnumSerializer.class)
    public enum OffsetReset {SMALLEST, LARGEST};

    @JsonProperty("id")
    public final String id;
    @JsonProperty("format")
    public final Format format;
    @JsonProperty("auto.offset.reset")
    public final OffsetReset autoOffsetReset;
    @JsonProperty("auto.commit.enable")
    public final boolean autoCommitEnable;

    public ConsumerData(String id, Format format,
            OffsetReset autoOffsetReset, boolean autoCommitEnable) {
        this.id = id;
        this.format = format;
        this.autoOffsetReset = autoOffsetReset;
        this.autoCommitEnable = autoCommitEnable;
    }
}
