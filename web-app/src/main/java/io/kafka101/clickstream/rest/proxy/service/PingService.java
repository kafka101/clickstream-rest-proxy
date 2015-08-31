package io.kafka101.clickstream.rest.proxy.service;

import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

@Path("/ping")
public class PingService {

	@GET
	@ManagedAsync
	public void ping(@Suspended final AsyncResponse response) {
		response.resume(Response.status(Response.Status.OK).entity("pong").build());
	}
}
