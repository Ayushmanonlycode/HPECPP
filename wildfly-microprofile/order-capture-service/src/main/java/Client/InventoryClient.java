package Client;

import Models.Dto.InventoryDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

import java.util.List;

@Path("/inventory")
@RegisterRestClient(configKey = "inventory-service")
@RegisterClientHeaders(JwtPropagationHeadersFactory.class)
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