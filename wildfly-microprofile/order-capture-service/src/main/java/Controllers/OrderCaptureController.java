package Controllers;

import Models.Dto.OrderDto;
import Models.Dto.OrderResponseDto;
import Models.Orders;
import Service.OrderCaptureService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/orders")
public class OrderCaptureController {

    @Inject
    OrderCaptureService orderService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(OrderDto orderDto) {
        Orders order= orderService.placeOrder(orderDto);


        if(order!=null){
            orderDto.setId(order.getId());
            return Response.ok(orderDto).build();
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
    public OrderResponseDto getOrder(@PathParam("id") UUID id) {
        return orderService.getByOid(id);

    }
}
