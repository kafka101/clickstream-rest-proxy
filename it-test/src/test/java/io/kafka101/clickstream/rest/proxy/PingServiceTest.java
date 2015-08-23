package io.kafka101.clickstream.rest.proxy;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import org.junit.Test;

public class PingServiceTest {
	private static String PING_URL = "http://localhost:8080/ping";

	@Test
	public void ping() throws Exception {
		Client client = Client.create();
		WebResource webResource = client.resource(PING_URL);
		String response = webResource.get(String.class);

		assertThat(response, is("pong"));
	}
}
