package Controllers;

import Models.Item;
import Service.ItemService;
import Service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/item")
public class ItemController {

    @Inject
    ItemService itemService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> getItems() {

        return itemService.getItems();

    }


    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> getItemsById(@QueryParam("id") String id) {
        return itemService.getItemsById(id);
    }

    @GET
    @Path("/search/productId")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> getItemsByProductId(@QueryParam("pid") String pid) {
        return itemService.getItemsByProductId(pid);
    }


    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(Item item){
        int res= itemService.addItem(item);

        if(res==0){
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
