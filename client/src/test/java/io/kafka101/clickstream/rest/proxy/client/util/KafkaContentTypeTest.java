package io.kafka101.clickstream.rest.proxy.client.util;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class KafkaContentTypeTest {

    @Test
    public void avro() {
        assertThat(KafkaContentType.avro(), is(equalTo("application/vnd.kafka.avro.v1+json")));
    }

    @Test
    public void binary() {
        assertThat(KafkaContentType.binary(), is(equalTo("application/vnd.kafka.binary.v1+json")));
    }

    @Test
    public void accept() {
        assertThat(KafkaContentType.accept(),
                is(equalTo("application/vnd.kafka.v1+json, application/vnd.kafka+json, application/json")));
    }

    @Test
    public void simpleAccept() {
        assertThat(KafkaContentType.simpleAccept(), is(equalTo("application/vnd.kafka.v1+json")));
    }
}
