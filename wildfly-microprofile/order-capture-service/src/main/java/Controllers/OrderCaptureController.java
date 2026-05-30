package Controllers;

import Models.Dto.OrderDto;
import Service.OrderCaptureService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
}
