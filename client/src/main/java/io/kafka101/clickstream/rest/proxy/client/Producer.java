package io.kafka101.clickstream.rest.proxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.kafka101.clickstream.rest.proxy.client.dto.PublishingData;
import io.kafka101.clickstream.rest.proxy.client.dto.PublishingResponse;
import io.kafka101.clickstream.rest.proxy.client.dto.Record;
import io.kafka101.clickstream.rest.proxy.client.util.HttpCallback;
import io.kafka101.clickstream.rest.proxy.client.util.KafkaContentType;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.client.HttpAsyncClient;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class Producer extends AbstractClient {

    private final Map<Class<?>, Schema> schemaCache = new ConcurrentHashMap<>();

    public Producer(String topics, HttpAsyncClient httpClient) throws URISyntaxException {
        super(topics, httpClient);
    }

    public Producer(URI topics, HttpAsyncClient httpClient) {
        super(topics, httpClient);
    }

    public <T> CompletableFuture<PublishingResponse> publish(T message, String topic)
            throws JsonProcessingException, UnsupportedEncodingException {
        Schema schema = namespacelessSchemaFor(message.getClass());
        return publish(schema, message, topic);
    }

    public <T> CompletableFuture<PublishingResponse> publish(Schema schema, T message, String topic)
            throws JsonProcessingException, UnsupportedEncodingException {

        PublishingData data = new PublishingData(new Record(message), null, null, schema.toString(), null);
        HttpCallback<PublishingResponse> callback = new HttpCallback(PublishingResponse.class);

        executePost(baseUri.resolve(topic),
                new StringEntity(mapper.writeValueAsString(data), ContentType.APPLICATION_JSON),
                callback);

        return callback;
    }

    private void executePost(URI topic, HttpEntity entity, FutureCallback<HttpResponse> callback) {
        HttpPost post = new HttpPost(topic);
        post.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, KafkaContentType.avro()));
        post.addHeader(new BasicHeader(HttpHeaders.ACCEPT, KafkaContentType.allTypes()));
        post.setEntity(entity);
        httpClient.execute(post, callback);
    }

    private Schema namespacelessSchemaFor(Class<?> type) {
        return schemaCache.computeIfAbsent(type, clazz -> {
            Schema schema = ReflectData.get().getSchema(clazz);
            // kind of a hack to set an empty namespace :)
            return new Schema.Parser().parse(schema.toString().replace(schema.getNamespace(), ""));
        });
    }
}
