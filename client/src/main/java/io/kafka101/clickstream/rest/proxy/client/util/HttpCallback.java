package io.kafka101.clickstream.rest.proxy.client.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class HttpCallback<T> implements FutureCallback<HttpResponse> {
    private final CompletableFuture<T> future;
    private final ObjectMapper mapper = new ObjectMapper();
    private final JavaType javaType;

    public HttpCallback(CompletableFuture<T> future, JavaType javaType) {
        this.future = future;
        this.javaType = javaType;
    }

    public HttpCallback(CompletableFuture<T> future, Class<T> responseClass) {
        this.future = future;
        this.javaType = mapper.getTypeFactory().constructType(responseClass);
    }

    @Override
    public void completed(HttpResponse response) {
        parseHttpResponse(response, future);
    }

    @Override
    public void failed(Exception ex) {
        future.completeExceptionally(ex);
    }

    @Override
    public void cancelled() {
        future.cancel(true);
    }

    private void parseHttpResponse(HttpResponse httpResponse, CompletableFuture<T> future) {
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            parseResponse(httpResponse, future);
        } else {
            parseError(httpResponse, future);
        }
    }

    private void parseResponse(HttpResponse httpResponse, CompletableFuture<T> future) {
        try {
            InputStream inputStream = httpResponse.getEntity().getContent();
            future.complete(mapper.readValue(inputStream, javaType));
            inputStream.close();
        } catch (IOException exception) {
            future.completeExceptionally(exception);
        }
    }

    private void parseError(HttpResponse httpResponse, CompletableFuture<T> future) {
        try {
            InputStream inputStream = httpResponse.getEntity().getContent();
            KafkaRestException error = mapper.readValue(inputStream, KafkaRestException.class);
            inputStream.close();
            future.completeExceptionally(error);
        } catch (IOException exception) {
            future.completeExceptionally(exception);
        }
    }
}