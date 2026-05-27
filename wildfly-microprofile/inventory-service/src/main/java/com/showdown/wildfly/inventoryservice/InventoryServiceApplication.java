package com.showdown.wildfly.inventoryservice;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class InventoryServiceApplication extends Application {
    @Path("/health")
    public static class HealthResource {
        @GET @Produces("text/plain")
        public String health() {
            return "Inventory Service is up and running!!!";
        }
    }
}
