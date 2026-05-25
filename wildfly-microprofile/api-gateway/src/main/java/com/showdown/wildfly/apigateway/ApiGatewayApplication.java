package com.showdown.wildfly.apigateway;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class ApiGatewayApplication extends Application {

    @Path("/health")
    public static class HealthResource {
        @GET
        @Produces("text/plain")
        public String health() {
            return "api-gateway running";
        }
    }
}
