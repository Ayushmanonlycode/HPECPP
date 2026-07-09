package com.showdown.wildfly.orderfulfillmentservice;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT")
@ApplicationPath("/")
public class OrderFulfillmentServiceApplication extends Application {
    @Path("/health")
    public static class HealthResource {
        @GET @Produces("text/plain")
        public String health() { return "order-fulfillment-service running"; }
    }
}
