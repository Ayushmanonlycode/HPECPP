package Controllers;

import Models.DTO.ItemDto;
import Models.Item;
import Service.ItemService;
import Service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Path("/items")
@PermitAll
public class ItemController {

    @Inject
    ItemService itemService;



    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ItemDto getItemsById(@PathParam("id") String id) {
        return itemService.getItemsById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ItemDto> getItemsByProductId(@QueryParam("productId") String pid) {
        if(pid == null){
            return itemService.getItems();
        }
        return itemService.getItemsByProductId(pid);
    }


    @GET
    @Path("/sku/{sku}")
    @Produces(MediaType.APPLICATION_JSON)
    public ItemDto getItemsBySku(@PathParam("sku") String sku) {
        return itemService.getItemsBySku(sku);
    }


    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(ItemDto item){
        int res= itemService.addItem(item);

        if(res==0){
            return Response.ok("Item added Successfully!!!").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
