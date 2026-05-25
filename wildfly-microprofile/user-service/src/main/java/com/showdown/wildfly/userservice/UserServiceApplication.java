package com.showdown.wildfly.userservice;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class UserServiceApplication extends Application {

    @Path("/health")
    public static class HealthResource {
        @GET
        @Produces("text/plain")
        public String health() {
            return "user-service running";
        }
    }
}
