package com.showdown.wildfly.cartservice.Controllers;

import com.showdown.wildfly.cartservice.Models.Cart;
import com.showdown.wildfly.cartservice.Models.CartItem;
import com.showdown.wildfly.cartservice.Models.DTO.AddToCartRequest;
import com.showdown.wildfly.cartservice.Models.DTO.CartResponse;
import com.showdown.wildfly.cartservice.Models.DTO.QuantityUpdateRequest;
import com.showdown.wildfly.cartservice.Service.CartService;
import com.showdown.wildfly.cartservice.Models.DTO.CartResponse;
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
    public CartResponse  getCart(@PathParam("userId") String userId) {
        return service.buildCartResponse(
        service.getCart(userId));
    }

    @POST
    @Path("/create")
    @Transactional
    public String createCart(Cart cart) {

        service.createCart(cart);

        return "Cart Created Successfully";
    }
   @POST
    @Path("/{userId}/items")
    @Transactional
    public CartResponse addItemFrontend(
            @PathParam("userId") String userId,
            AddToCartRequest request) {

        Cart cart = service.getCart(userId);

        if (cart == null) {
            return null;
        }

        CartItem item = new CartItem();

        item.setSku(request.getItemSku());
        item.setProductName(request.getProductName());
        item.setPrice(request.getUnitPrice());
        item.setQuantity(request.getQuantity());
        item.setCart(cart);

        service.addItem(item);

        return service.buildCartResponse(
        service.getCart(userId));
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
    @PUT
    @Path("/{userId}/items/{sku}")
    @Transactional
    public CartResponse updateQuantityFrontend(
            @PathParam("userId") String userId,
            @PathParam("sku") String sku,
            QuantityUpdateRequest request) {

        service.updateQuantity(sku, request.getQuantity());

        return service.buildCartResponse(
        service.getCart(userId));
    }
    @DELETE
    @Path("/item/{sku}")
    @Transactional
    public String removeItem(@PathParam("sku") String sku) {

        service.removeItem(sku);

        return "Item Removed Successfully";
    }
    @DELETE
    @Path("/{userId}/items/{sku}")
    @Transactional
    public CartResponse removeItemFrontend(
            @PathParam("userId") String userId,
            @PathParam("sku") String sku) {

        service.removeItem(sku);

        return service.buildCartResponse(
        service.getCart(userId));
    }
    @DELETE
    @Path("/{userId}")
    @Transactional
    public String clearCart(@PathParam("userId") String userId) {

        service.clearCart(userId);

        return "Cart Cleared Successfully";
    }
}