package io.kafka101.clickstream.rest.proxy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/ping")
public class PingService {

	@GET()
	public String ping() {
		return "pong";
	}
}
