package io.kafka101.clickstream.rest.proxy;

import io.kafka101.clickstream.rest.proxy.service.PingService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PingServiceTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(PingService.class);
	}

	@Test
	public void pingSync() {
		String response = target()
				.path("ping")
				.request()
				.get(String.class);
		assertThat(response, is("pong"));
	}

	@Test
	public void pingAsync() throws InterruptedException, ExecutionException, TimeoutException {
		Future<String> response = target()
				.path("ping")
				.request().async()
				.get(String.class);

		assertThat(response.get(3000, TimeUnit.MILLISECONDS), is("pong"));
	}
}
