package com.showdown.wildfly.orderfulfillmentservice.Controllers;

import com.showdown.wildfly.orderfulfillmentservice.Models.OrderFulfillment;
import com.showdown.wildfly.orderfulfillmentservice.Service.OrderFulfillmentService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Path("/fulfillment")
@RolesAllowed({"ADMIN", "CUSTOMER"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderFulfillmentController {

    @Inject
    OrderFulfillmentService service;

    @POST
    @Path("/add")
    @Transactional
    public String addOrder(OrderFulfillment order) {
        service.addOrder(order);
        return "Order Added Successfully";
    }

    @GET
    @Path("/all")
    public List<OrderFulfillment> getAllOrders() {
        return service.getAllOrders();
    }

    @GET
    @Path("/{id}")
    public OrderFulfillment getOrderById(@PathParam("id") String id) {
        return service.getOrderById(id);
    }

    @PUT
    @Path("/update")
    @Transactional
    public String updateOrder(OrderFulfillment order) {
        service.updateOrder(order);
        return "Order Updated Successfully";
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public String deleteOrder(@PathParam("id") String id) {
        service.deleteOrder(id);
        return "Order Deleted Successfully";
    }
    @GET
    @Path("/status/{status}")
    public List<OrderFulfillment> getOrdersByStatus(
            @PathParam("status") String status) {

        return service.getOrdersByStatus(status);
    }
    @PUT
    @Path("/ship/{id}")
    @Transactional
    public String shipOrder(@PathParam("id") String id) {

        service.shipOrder(id);

        return "Order Shipped Successfully";
    }

    @PUT
    @Path("/deliver/{id}")
    @Transactional
    public String deliverOrder(@PathParam("id") String id) {

        service.deliverOrder(id);

        return "Order Delivered Successfully";
    }

    @PUT
    @Path("/cancel/{id}")
    @Transactional
    public String cancelOrder(@PathParam("id") String id) {

        service.cancelOrder(id);

        return "Order Cancelled Successfully";
    }
    @PUT
    @Path("/process/{id}")
    @Transactional
    public String processOrder(@PathParam("id") String id) {

        service.processOrder(id);

        return "Order Processing Started";
    }
    @PUT
    @Path("/fail/{id}")
    @Transactional
    public String failOrder(@PathParam("id") String id) {

        service.failOrder(id);

        return "Order Marked As Failed";
    }
    @GET
    @Path("/tracking/{trackingNumber}")
    public OrderFulfillment getByTrackingNumber(
            @PathParam("trackingNumber") String trackingNumber) {

        return service.getByTrackingNumber(trackingNumber);
    }
    @GET
    @Path("/count")
    public long countOrders() {
        return service.countOrders();
    }
    @GET
    @Path("/revenue")
    public double getTotalRevenue() {
        return service.getTotalRevenue();
    }
}