package io.kafka101.clickstream.rest.proxy.client.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

public final class AsyncClientWrapper {
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setSocketTimeout(3000)
            .setConnectTimeout(3000).build();
    private static final CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
            .setDefaultRequestConfig(REQUEST_CONFIG)
            .setMaxConnPerRoute(1000)
            .setMaxConnTotal(1000)
            .build();

    private static volatile AsyncClientWrapper cachedClient;

    private AsyncClientWrapper() {
        httpClient.start();
    }

    public static AsyncClientWrapper getInstance() {
        AsyncClientWrapper result = cachedClient;
        if (result == null) { // First check (no locking)
            synchronized (AsyncClientWrapper.class) {
                result = cachedClient;
                if (result == null) { // Second check (with locking)
                    cachedClient = result = new AsyncClientWrapper();
                }
            }
        }
        return result;
    }

    public CloseableHttpAsyncClient getWrappedClient() {
        return httpClient;
    }
}
