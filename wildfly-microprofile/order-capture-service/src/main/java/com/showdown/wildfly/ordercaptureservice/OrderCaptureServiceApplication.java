package com.showdown.wildfly.ordercaptureservice;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT")
@ApplicationPath("/")
public class OrderCaptureServiceApplication extends Application {
    @Path("/health")
    public static class HealthResource {
        @GET @Produces("text/plain")
        public String health() { return "Order Capture is Running"; }
    }
}
