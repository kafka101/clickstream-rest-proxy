package io.kafka101.clickstream.rest.proxy.client.util;

import org.apache.http.entity.ContentType;

/**
 * Kafka's content type format: application/vnd.kafka[.embedded_format][.api_version]+[serialization_format]
 */
public class KafkaContentType {

    private static final String TEMPLATE = "application/vnd.kafka%s%s+%s";

    public enum ApiVersion {
        NONE(""), V1(".v1");
        public final String arg;
        ApiVersion(String arg) {
            this.arg = arg;
        }
    }

    public enum SerializationFormat {
        JSON("json");
        public final String arg;
        SerializationFormat(String arg) {
            this.arg = arg;
        }
    }

    public enum EmbeddedFormat {
        NONE(""), AVRO(".avro"), BINARY(".binary");
        public final String arg;
        EmbeddedFormat(String arg) {
            this.arg = arg;
        }
    }

    public static final String generate(SerializationFormat serialization, ApiVersion version, EmbeddedFormat embedded) {
        return String.format(TEMPLATE, embedded.arg, version.arg, serialization.arg);
    }

    /**
     * @return <code>application/vnd.kafka.v1+json, application/vnd.kafka+json, application/json</code>
     */
    public static final String accept() {
        return String.format("%s, %s, %s",
                generate(SerializationFormat.JSON, ApiVersion.V1, EmbeddedFormat.NONE),
                generate(SerializationFormat.JSON, ApiVersion.NONE, EmbeddedFormat.NONE),
                ContentType.APPLICATION_JSON.getMimeType());
    }

    /**
     * @return <code>application/vnd.kafka.v1+json</code>
     */
    public static final String simpleAccept() {
        return generate(SerializationFormat.JSON, ApiVersion.V1, EmbeddedFormat.NONE);
    }

    public static final String binary() {
        return generate(SerializationFormat.JSON, ApiVersion.V1, EmbeddedFormat.BINARY);
    }

    public static final String avro() {
        return generate(SerializationFormat.JSON, ApiVersion.V1, EmbeddedFormat.AVRO);
    }
}
