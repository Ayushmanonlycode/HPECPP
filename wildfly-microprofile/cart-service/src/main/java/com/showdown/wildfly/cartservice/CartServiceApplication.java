package com.showdown.wildfly.cartservice;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class CartServiceApplication extends Application {
    @Path("/health")
    public static class HealthResource {
        @GET @Produces("text/plain")
        public String health() { return "cart-service running"; }
    }
}
