package io.kafka101.clickstream.rest.proxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import io.kafka101.clickstream.rest.proxy.client.dto.ConsumerData;
import io.kafka101.clickstream.rest.proxy.client.dto.ConsumerRecord;
import io.kafka101.clickstream.rest.proxy.client.dto.ConsumerResponse;
import io.kafka101.clickstream.rest.proxy.client.util.HttpCallback;
import io.kafka101.clickstream.rest.proxy.client.util.KafkaContentType;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.client.HttpAsyncClient;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Consumer extends AbstractClient {

    private static final String CONSUME_URI_TEMPLATE = "%s/instances/%s/topics/%s/";
    private static final String DELETE_URI_TEMPLATE = "%s/instances/%s/";

    public Consumer(String consumers, HttpAsyncClient httpClient) throws URISyntaxException {
        super(consumers, httpClient);
    }

    public Consumer(URI consumers, HttpAsyncClient httpClient) {
        super(consumers, httpClient);
    }

    public CompletableFuture<ConsumerResponse> create(String group, ConsumerData data)
            throws JsonProcessingException, UnsupportedEncodingException {
        HttpPost post = new HttpPost(baseUri.resolve(group));
        post.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, KafkaContentType.avro()));
        post.setEntity(new StringEntity(mapper.writeValueAsString(data), ContentType.APPLICATION_JSON));

        HttpCallback<ConsumerResponse> callback = new HttpCallback<>(ConsumerResponse.class);
        httpClient.execute(post, callback);

        return callback;
    }

    public CompletableFuture<ConsumerResponse> create(String group, String id, ConsumerData.Format format,
            ConsumerData.OffsetReset autoOffsetReset, boolean autoCommitEnable)
            throws JsonProcessingException, UnsupportedEncodingException {
        ConsumerData data = new ConsumerData(id, format, autoOffsetReset, autoCommitEnable);
        return create(group, data);
    }

    public <K, V> CompletableFuture<List<ConsumerRecord<K, V>>> consume(String group, String id, String topic,
            Class<K> keyClass, Class<V> valueClazz) {
        HttpGet get = new HttpGet(baseUri.resolve(String.format(CONSUME_URI_TEMPLATE, group, id, topic)));
        get.addHeader(new BasicHeader(HttpHeaders.ACCEPT, KafkaContentType.avro()));

        HttpCallback<List<ConsumerRecord<K, V>>> callback = new HttpCallback<>(constructType(keyClass, valueClazz));
        httpClient.execute(get, callback);
        return callback;
    }

    public void destroy(String group, String id) throws ExecutionException, InterruptedException {
        HttpDelete delete = new HttpDelete(baseUri.resolve(String.format(DELETE_URI_TEMPLATE, group, id)));
        delete.addHeader(new BasicHeader(HttpHeaders.ACCEPT, KafkaContentType.allTypes()));
        httpClient.execute(delete, null).get();
    }

    private JavaType constructType(Class keyClass, Class valueClazz) {
        JavaType type = mapper.getTypeFactory().constructParametrizedType(ConsumerRecord.class, ConsumerRecord.class,
                keyClass, valueClazz);
        return mapper.getTypeFactory().constructCollectionType(List.class, type);
    }
}
