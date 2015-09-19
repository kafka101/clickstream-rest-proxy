package io.kafka101.clickstream.rest.proxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.kafka101.clickstream.rest.proxy.client.avro.AvroTranslator;
import io.kafka101.clickstream.rest.proxy.client.dto.PostPayload;
import io.kafka101.clickstream.rest.proxy.client.dto.Record;
import io.kafka101.clickstream.rest.proxy.client.dto.Response;
import io.kafka101.clickstream.rest.proxy.client.util.HttpPublishCallback;
import io.kafka101.clickstream.rest.proxy.client.util.KafkaContentType;
import org.apache.avro.Schema;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public class Producer {

    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setSocketTimeout(3000)
            .setConnectTimeout(3000).build();
    private final CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
            .setDefaultHeaders(Lists.newArrayList(
                    new BasicHeader(HttpHeaders.CONTENT_TYPE, KafkaContentType.avro()),
                    new BasicHeader(HttpHeaders.ACCEPT, KafkaContentType.accept())))
            .setDefaultRequestConfig(REQUEST_CONFIG)
            .setMaxConnPerRoute(1000)
            .setMaxConnTotal(1000)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final AvroTranslator avroTranslator = new AvroTranslator();
    private final URI topics;

    public Producer(String topics) throws URISyntaxException {
        this(new URI(topics));
    }

    public Producer(URI topics) {
        this.topics = topics;
    }

    public void start() {
        httpClient.start();
    }

    public void close() throws IOException {
        httpClient.close();
    }

    public <T> CompletableFuture<Response> publish(T message, String topic)
            throws JsonProcessingException, UnsupportedEncodingException {
        Schema schema = avroTranslator.namespacelessSchemaFor(message.getClass());
        return publish(schema, message, topic);
    }

    public <T> CompletableFuture<Response> publish(Schema schema, T message, String topic)
            throws JsonProcessingException, UnsupportedEncodingException {

        HttpPost post = new HttpPost(topics.resolve(topic));
        PostPayload request = new PostPayload(new Record(message), null, null, schema.toString(), null);
        post.setEntity(new StringEntity(mapper.writeValueAsString(request), ContentType.APPLICATION_JSON));
        CompletableFuture<Response> future = new CompletableFuture();
        httpClient.execute(post, new HttpPublishCallback(future));

        return future;
    }
}
