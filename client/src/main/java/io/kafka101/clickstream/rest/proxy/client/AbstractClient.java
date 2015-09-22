package io.kafka101.clickstream.rest.proxy.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.nio.client.HttpAsyncClient;

import java.net.URI;
import java.net.URISyntaxException;

abstract class AbstractClient {
    final URI baseUri;
    final HttpAsyncClient httpClient;
    final ObjectMapper mapper = new ObjectMapper();

    public AbstractClient(String baseUri, HttpAsyncClient httpClient) throws URISyntaxException {
        this(new URI(baseUri), httpClient);
    }

    public AbstractClient(URI baseUri, HttpAsyncClient httpClient) {
        this.baseUri = baseUri;
        this.httpClient = httpClient;
    }
}
