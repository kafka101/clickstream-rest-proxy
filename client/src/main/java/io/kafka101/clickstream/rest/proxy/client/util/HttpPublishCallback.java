package io.kafka101.clickstream.rest.proxy.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kafka101.clickstream.rest.proxy.client.dto.Response;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class HttpPublishCallback implements FutureCallback<HttpResponse> {
    private final CompletableFuture<Response> future;
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpPublishCallback(CompletableFuture<Response> future) {
        this.future = future;
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

    private void parseHttpResponse(HttpResponse httpResponse, CompletableFuture<Response> future) {
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            parseResponse(httpResponse, future);
        } else {
            parseError(httpResponse, future);
        }
    }

    private void parseResponse(HttpResponse httpResponse, CompletableFuture<Response> future) {
        try {
            InputStream inputStream = httpResponse.getEntity().getContent();
            future.complete(mapper.readValue(inputStream, Response.class));
        } catch (IOException exception) {
            future.completeExceptionally(exception);
        }
    }

    private void parseError(HttpResponse httpResponse, CompletableFuture<Response> future) {
        try {
            InputStream inputStream = httpResponse.getEntity().getContent();
            KafkaRestException error = mapper.readValue(inputStream, KafkaRestException.class);
            future.completeExceptionally(error);
        } catch (IOException exception) {
            future.completeExceptionally(exception);
        }
    }
}