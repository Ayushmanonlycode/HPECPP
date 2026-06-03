package Controllers;

import Models.Dto.OrderDto;
import Models.Dto.OrderResponseDto;
import Service.OrderCaptureService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/order")
public class OrderCaptureController {

    @Inject
    OrderCaptureService orderService;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(OrderDto orderDto) {
        int res= orderService.placeOrder(orderDto);

        if(res==0){
            return Response.ok("Order Placed Successfully").build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Failed to place order")
                .build();

    }


    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }


    @GET
    @Path("/search/cid")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderResponseDto> searchByCid(@QueryParam("cid") String cid) {
        return orderService.getByCid(cid);

    }


    @GET
    @Path("/search/oid")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderResponseDto> searchByOid(@QueryParam("oid") String oid) {
        return orderService.getByOid(oid);

    }
}
