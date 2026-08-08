package com.showdown.wildfly.cartservice;

import org.eclipse.microprofile.auth.LoginConfig;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;

@LoginConfig(authMethod = "MP-JWT")
@ApplicationPath("/")
public class CartServiceApplication extends Application {
    @Path("/health")
    public static class HealthResource {
        @GET @Produces("text/plain")
        public String health() { return "cart-service running"; }
    }
}
