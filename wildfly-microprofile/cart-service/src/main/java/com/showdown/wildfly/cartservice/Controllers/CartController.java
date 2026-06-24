package com.showdown.wildfly.cartservice.Controllers;

import com.showdown.wildfly.cartservice.Models.CartItem;
import com.showdown.wildfly.cartservice.Models.DTO.AddToCartRequest;
import com.showdown.wildfly.cartservice.Models.DTO.CartResponse;
import com.showdown.wildfly.cartservice.Models.DTO.QuantityUpdateRequest;
import com.showdown.wildfly.cartservice.Service.CartService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartController {

    @Inject
    CartService service;

    @GET
    @Path("/{userId}")
    public CartResponse  getCart(@PathParam("userId") String userId) {
        return service.buildCartResponse(
        service.getCart(userId));
    }


    @POST
    @Path("/{userId}/items")
    @Transactional
    public CartResponse addItem(
            @PathParam("userId") String userId,
            AddToCartRequest request) {

        CartItem item = new CartItem();

        item.setSku(request.getItemSku());
        item.setProductName(request.getProductName());
        item.setPrice(request.getUnitPrice());
        item.setQuantity(request.getQuantity());

        service.addItem(userId, item);

        return service.buildCartResponse(
                service.getCart(userId));
    }

    @PUT
    @Path("/{userId}/items/{sku}")
    @Transactional
    public CartResponse updateItemQuantity(
            @PathParam("userId") String userId,
            @PathParam("sku") String sku,
            QuantityUpdateRequest request) {

        service.updateQuantity(userId, sku, request.getQuantity());

        return service.buildCartResponse(
        service.getCart(userId));
    }

    @DELETE
    @Path("/{userId}/items/{sku}")
    @Transactional
    public CartResponse removeItem(
            @PathParam("userId") String userId,
            @PathParam("sku") String sku) {

        service.removeItem(userId, sku);

        return service.buildCartResponse(
        service.getCart(userId));
    }
    @DELETE
    @Path("/{userId}")
    @Transactional
    public Response clearCart(@PathParam("userId") String userId) {

        service.clearCart(userId);

        return Response.noContent().build();
    }
}