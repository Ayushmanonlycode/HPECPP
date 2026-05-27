package Controllers;

import Models.Inventory;
import Service.InventoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/inventory")
public class InventoryController {

    @Inject
    InventoryService inventoryService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Inventory> getAllInventory() {

        return inventoryService.getAllInventory();

    }

    @GET
    @Path("/item")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Inventory> getInventoryByItemId(@QueryParam("itemId") String itemId) {
        return inventoryService.getInventoryByItemId(itemId);
    }


    @PUT
    @Path("/update")
    public Response updateInventory(@QueryParam("itemId") String itemId, @QueryParam("quantity") int quantity) {
        int res= inventoryService.updateInventory(itemId, quantity);
        if(res==0) {
            return Response.ok("Inventory updated successfully").build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Item not found")
                .build();
    }


    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addInventory(Inventory inventory) {
        int res= inventoryService.addInventory(inventory);

        if(res==0){
            return Response.ok("Item added to the inventory successfully").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
