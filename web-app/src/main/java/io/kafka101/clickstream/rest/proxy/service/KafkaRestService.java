package io.kafka101.clickstream.rest.proxy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.kafka101.clickstream.rest.proxy.client.Producer;
import io.kafka101.clickstream.rest.proxy.client.util.AsyncClientWrapper;
import io.kafka101.clickstream.rest.proxy.domain.Click;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/actions")
public class KafkaRestService {

    Producer producer;

    public KafkaRestService() throws URISyntaxException {
        producer = new Producer("http://127.0.0.1:8082/topics/", AsyncClientWrapper.getInstance().getWrappedClient());
    }

    @Path("click")
    @POST
    @ManagedAsync
    @Consumes(APPLICATION_JSON)
    public void click(Click click, @Context UriInfo uriInfo, @HeaderParam("user-agent") String userAgent,
            @Suspended final AsyncResponse response)
            throws MalformedURLException, UnsupportedEncodingException, JsonProcessingException {
        click.setUserAgent(userAgent);
        click.setIp(uriInfo.getRequestUri().toURL());
        producer.publish(click, "click");
        response.resume(Response.status(NO_CONTENT).build());
    }
}
