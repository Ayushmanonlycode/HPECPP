package com.showdown.wildfly.orderfulfilmentservice;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class OrderFulfilmentServiceApplication extends Application {
    @Path("/health")
    public static class HealthResource {
        @GET @Produces("text/plain")
        public String health() { return "order-fulfilment-service running"; }
    }
}
