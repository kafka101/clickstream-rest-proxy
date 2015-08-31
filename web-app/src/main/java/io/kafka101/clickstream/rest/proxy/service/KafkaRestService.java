package io.kafka101.clickstream.rest.proxy.service;

import io.kafka101.clickstream.rest.proxy.client.KafkaClient;
import io.kafka101.clickstream.rest.proxy.domain.Click;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.MalformedURLException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/actions")
public class KafkaRestService {

    KafkaClient kafkaClient;

    @Path("click")
    @POST
    @ManagedAsync
    @Consumes(APPLICATION_JSON)
    public void click(Click click, @Context UriInfo uriInfo, @HeaderParam("user-agent") String userAgent,
            @Suspended final AsyncResponse response) throws MalformedURLException {
        click.setUserAgent(userAgent);
        click.setIp(uriInfo.getRequestUri().toURL());

        System.out.println(click);

        response.resume(Response.status(NO_CONTENT).build());
    }
}
