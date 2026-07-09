package Controllers;

import Models.Dto.OrderDto;
import Models.Dto.OrderResponseDto;
import Models.Orders;
import Service.OrderCaptureService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.UUID;

@Path("/orders")
@PermitAll
public class OrderCaptureController {

    @Inject
    OrderCaptureService orderService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrder(OrderDto orderDto) {
        try {
            Orders order = orderService.placeOrder(orderDto);

            orderDto.setId(order.getId());

            return Response.ok(orderDto).build();

        } catch (RuntimeException e) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(java.util.Map.of("message", e.getMessage()))
                    .build();
        }

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