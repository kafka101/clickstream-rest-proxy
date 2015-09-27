package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PublishingData {
    @JsonProperty("records")
    public final List<Record> records;
    @JsonProperty("key_schema")
    public final String keySchema;
    @JsonProperty("key_schema_id")
    public final Integer keySchemaId;
    @JsonProperty("value_schema")
    public final String valueSchema;
    @JsonProperty("value_schema_id")
    public final Integer valueSchemaId;

    public PublishingData(Record record, String keySchema, Integer keySchemaId, String valueSchema,
            Integer valueSchemaId) {
        this(Lists.newArrayList(record), keySchema, keySchemaId,  valueSchema, valueSchemaId);
    }

    public PublishingData(List<Record> records, String keySchema, Integer keySchemaId, String valueSchema,
            Integer valueSchemaId) {
        this.records = records;
        this.keySchema = keySchema;
        this.keySchemaId = keySchemaId;
        this.valueSchema = valueSchema;
        this.valueSchemaId = valueSchemaId;
    }
}