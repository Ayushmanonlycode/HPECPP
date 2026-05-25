package com.showdown.wildfly.ordercaptureservice;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class OrderCaptureServiceApplication extends Application {
    @Path("/health")
    public static class HealthResource {
        @GET @Produces("text/plain")
        public String health() { return "order-capture-service running"; }
    }
}
