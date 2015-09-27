package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kafka101.clickstream.rest.proxy.client.Person;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class SerializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void parsesPublishingResponse() throws IOException {
        String json = "{\"offsets\":[{\"partition\":0,\"offset\":42,\"error_code\":null,\"error\":null}],\"keySchemaId\":null,\"value_schema_id\":1}";
        mapper.readValue(json, PublishingResponse.class);
    }

    @Test
    public void parsesResponse() throws IOException {
        String json = "[{\"key\":null,\"value\":{\"firstName\":\"Topper\",\"lastName\":\"Harley\"},\"partition\":0,\"offset\":0},{\"key\":null,\"value\":{\"firstName\":\"Topper\",\"lastName\":\"Harley\"},\"partition\":0,\"offset\":1}]";
        JavaType type = mapper.getTypeFactory().constructParametrizedType(ConsumerRecord.class, ConsumerRecord.class, String.class, Person.class);
        JavaType collection = mapper.getTypeFactory().constructCollectionType(List.class, type);
        List<ConsumerRecord<String, Person>> records = mapper.readValue(json, collection);
    }
}
