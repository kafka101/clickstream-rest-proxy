package io.kafka101.clickstream.rest.proxy.client.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class HttpCallback<T> extends CompletableFuture<T> implements FutureCallback<HttpResponse> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final JavaType javaType;

    public HttpCallback(JavaType javaType) {
        this.javaType = javaType;
    }

    public HttpCallback(Class<T> responseClass) {
        this.javaType = mapper.getTypeFactory().constructType(responseClass);
    }

    @Override
    public void completed(HttpResponse response) {
        parseHttpResponse(response);
    }

    @Override
    public void failed(Exception ex) {
        this.completeExceptionally(ex);
    }

    @Override
    public void cancelled() {
        this.cancel(true);
    }

    private void parseHttpResponse(HttpResponse httpResponse) {
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            parseResponse(httpResponse);
        } else {
            parseError(httpResponse);
        }
    }

    private void parseResponse(HttpResponse httpResponse) {
        try {
            InputStream inputStream = httpResponse.getEntity().getContent();
            this.complete(mapper.readValue(inputStream, javaType));
            inputStream.close();
        } catch (IOException exception) {
            this.completeExceptionally(exception);
        }
    }

    private void parseError(HttpResponse httpResponse) {
        try {
            InputStream inputStream = httpResponse.getEntity().getContent();
            KafkaRestException error = mapper.readValue(inputStream, KafkaRestException.class);
            inputStream.close();
            this.completeExceptionally(error);
        } catch (IOException exception) {
            this.completeExceptionally(exception);
        }
    }
}