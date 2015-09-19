package io.kafka101.clickstream.rest.proxy.client.avro;


public class AvroException extends RuntimeException {
    public AvroException(String message, Throwable cause) {
        super(message, cause);
    }
}