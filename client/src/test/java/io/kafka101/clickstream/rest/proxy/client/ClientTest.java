package io.kafka101.clickstream.rest.proxy.client;

import io.kafka101.clickstream.rest.proxy.client.dto.ConsumerData;
import io.kafka101.clickstream.rest.proxy.client.dto.ConsumerRecord;
import io.kafka101.clickstream.rest.proxy.client.dto.ConsumerResponse;
import io.kafka101.clickstream.rest.proxy.client.dto.PublishingResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);
    private static final String TOPICS_ENDPOINT = "http://127.0.0.1:8082/topics/";
    private static final String CONSUMERS_ENDPOINT = "http://127.0.0.1:8082/consumers/";
    private static final CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
    private volatile boolean messagePublished;
    private volatile boolean consumerCreated;
    private volatile boolean personRead;

    @Before
    public void setUp() {
        messagePublished = false;
        client.start();
    }

    @After
    public void tearDown() throws IOException {
        client.close();
    }

    @Test
    public void endToEndTest() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        Producer producer = new Producer(TOPICS_ENDPOINT, client);
        producer.publish(new Person("Topper", "Harley"), "HotShots")
                .whenComplete((response, throwable) -> accept(response, throwable));
        await().atMost(5, TimeUnit.SECONDS).until(() -> messagePublished);

        Consumer consumer = new Consumer(CONSUMERS_ENDPOINT, client);
        consumer.create("testGroup", "testInstance", ConsumerData.Format.AVRO, ConsumerData.OffsetReset.SMALLEST, true)
                .whenComplete((response, throwable) -> created(response, throwable));
        await().atMost(5, TimeUnit.SECONDS).until(() -> consumerCreated);

        consumer.consume("testGroup", "testInstance", "HotShots", String.class, Person.class)
                .whenComplete((response, throwable) -> accept(response, throwable));
        await().atMost(5, TimeUnit.SECONDS).until(() -> personRead);

        consumer.destroy("testGroup", "testInstance");
    }

    private void created(ConsumerResponse response, Throwable throwable) {
        if (response != null) {
            consumerCreated = true;
        } else {
            logger.error("Could not create consumer!", throwable);
        }
    }

    private void accept(List<ConsumerRecord<String, Person>> response, Throwable throwable) {
        if (response != null) {
            assertThat(response.size(), is(equalTo(1)));
            personRead = true;
        } else {
            logger.error("Could not read message!", throwable);
        }
    }

    private void accept(PublishingResponse response, Throwable throwable) {
        if (response != null) {
            messagePublished = true;
        } else {
            logger.error("Could not publish message!", throwable);
        }
    }
}
