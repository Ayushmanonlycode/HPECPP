package com.showdown.wildfly.cartservice.Controllers;

import com.showdown.wildfly.cartservice.Models.Cart;
import com.showdown.wildfly.cartservice.Models.CartItem;
import com.showdown.wildfly.cartservice.Service.CartService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartController {

    @Inject
    CartService service;

    @GET
    @Path("/{userId}")
    public Cart getCart(@PathParam("userId") String userId) {
        return service.getCart(userId);
    }

    @POST
    @Path("/create")
    @Transactional
    public String createCart(Cart cart) {

        service.createCart(cart);

        return "Cart Created Successfully";
    }

    @POST
    @Path("/item/add")
    @Transactional
    public String addItem(CartItem item) {

        service.addItem(item);

        return "Item Added Successfully";
    }

    @PUT
    @Path("/item/{sku}/{quantity}")
    @Transactional
    public CartItem updateQuantity(
            @PathParam("sku") String sku,
            @PathParam("quantity") int quantity) {

        return service.updateQuantity(sku, quantity);
    }

    @DELETE
    @Path("/item/{sku}")
    @Transactional
    public String removeItem(@PathParam("sku") String sku) {

        service.removeItem(sku);

        return "Item Removed Successfully";
    }

    @DELETE
    @Path("/{userId}")
    @Transactional
    public String clearCart(@PathParam("userId") String userId) {

        service.clearCart(userId);

        return "Cart Cleared Successfully";
    }
}