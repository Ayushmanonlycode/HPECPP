package com.showdown.springboot.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gateway.services")
public class GatewayProperties {

    private String userService = "http://localhost:8081";
    private String catalogService = "http://localhost:8082";
    private String inventoryService = "http://localhost:8083";
    private String orderCaptureService = "http://localhost:8084";
    private String orderFulfilmentService = "http://localhost:8085";
    private String cartService = "http://localhost:8086";

    public String getUserService() { return userService; }
    public void setUserService(String userService) { this.userService = userService; }

    public String getCatalogService() { return catalogService; }
    public void setCatalogService(String catalogService) { this.catalogService = catalogService; }

    public String getInventoryService() { return inventoryService; }
    public void setInventoryService(String inventoryService) { this.inventoryService = inventoryService; }

    public String getOrderCaptureService() { return orderCaptureService; }
    public void setOrderCaptureService(String orderCaptureService) { this.orderCaptureService = orderCaptureService; }

    public String getOrderFulfilmentService() { return orderFulfilmentService; }
    public void setOrderFulfilmentService(String orderFulfilmentService) { this.orderFulfilmentService = orderFulfilmentService; }

    public String getCartService() { return cartService; }
    public void setCartService(String cartService) { this.cartService = cartService; }
}
