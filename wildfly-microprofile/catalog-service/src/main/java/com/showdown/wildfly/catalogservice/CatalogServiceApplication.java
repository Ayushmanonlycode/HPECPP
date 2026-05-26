package com.showdown.wildfly.catalogservice;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class CatalogServiceApplication extends Application {
    @Path("/health")
    public static class HealthResource {
        @GET @Produces("text/plain")
        public String health() {
            return "Catalog Service Running!!!";
        }
    }
}
