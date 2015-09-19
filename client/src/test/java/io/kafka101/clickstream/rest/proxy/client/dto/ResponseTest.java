package io.kafka101.clickstream.rest.proxy.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

public class ResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void parseResponse() throws IOException {
        String json = "{\"offsets\":[{\"partition\":0,\"offset\":42,\"error_code\":null,\"error\":null}],\"keySchemaId\":null,\"value_schema_id\":1}";
        mapper.readValue(json, Response.class);
    }
}
