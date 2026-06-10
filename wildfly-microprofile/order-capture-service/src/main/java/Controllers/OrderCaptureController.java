package Controllers;

import Models.Dto.OrderDto;
import Models.Dto.OrderResponseDto;
import Service.OrderCaptureService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/orders")
public class OrderCaptureController {

    @Inject
    OrderCaptureService orderService;

    @POST
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }




    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderResponseDto> getUserOrders(
            @PathParam("userId") String userId) {
        return orderService.getByCid(userId);

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public OrderResponseDto getOrder(@PathParam("id") String id) {
        return orderService.getByOid(id);

    }
}
