package Client;

import Models.Dto.InventoryDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/inventory")
@RegisterRestClient(configKey = "inventory-service")
public interface InventoryClient {

    @GET
    @Path("/{sku}")
    InventoryDto getInventoryBySku(
            @PathParam("sku") String sku);

    @PUT
    @Path("/update/{sku}")
    Response updateInventory(
            @PathParam("sku") String sku,
            @QueryParam("quantity") int quantity);
}