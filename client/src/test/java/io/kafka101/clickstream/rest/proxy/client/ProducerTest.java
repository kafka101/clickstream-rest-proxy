package io.kafka101.clickstream.rest.proxy.client;

import io.kafka101.clickstream.rest.proxy.client.dto.Response;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;

public class ProducerTest {
    private static final Logger logger = LoggerFactory.getLogger(ProducerTest.class);
    private static final String KAFKA_PROXY_ENDPOINT = "http://127.0.0.1:8082/topics/";
    private volatile boolean messagePublished;

    @Before
    public void setUp() {
        messagePublished = false;
    }

    @Test
    public void test() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        Producer producer = new Producer(KAFKA_PROXY_ENDPOINT);
        producer.start();
        producer.publish(new Person("Topper", "Harley"), "HotShots")
                .whenComplete((response, throwable) -> accept(response, throwable));
        await().atMost(5, TimeUnit.SECONDS).until(() -> messagePublished);
        producer.close();
    }

    private void accept(Response response, Throwable throwable) {
        if (response != null) {
            messagePublished = true;
        } else {
            logger.error("Could not publish message!", throwable);
        }
    }
}
