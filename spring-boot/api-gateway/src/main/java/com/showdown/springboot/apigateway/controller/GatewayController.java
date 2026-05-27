package com.showdown.springboot.apigateway.controller;

import com.showdown.springboot.apigateway.config.GatewayProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Enumeration;

@RestController
public class GatewayController {

    private final RestTemplate restTemplate;
    private final GatewayProperties gatewayProperties;

    public GatewayController(RestTemplate restTemplate, GatewayProperties gatewayProperties) {
        this.restTemplate = restTemplate;
        this.gatewayProperties = gatewayProperties;
    }

    @RequestMapping("/api/users/**")
    public ResponseEntity<String> proxyUsers(HttpServletRequest request,
                                              @RequestBody(required = false) String body) {
        return forward(request, body, gatewayProperties.getUserService());
    }

    @RequestMapping("/api/categories/**")
    public ResponseEntity<String> proxyCategories(HttpServletRequest request,
                                                   @RequestBody(required = false) String body) {
        return forward(request, body, gatewayProperties.getCatalogService());
    }

    @RequestMapping("/api/products/**")
    public ResponseEntity<String> proxyProducts(HttpServletRequest request,
                                                 @RequestBody(required = false) String body) {
        return forward(request, body, gatewayProperties.getCatalogService());
    }

    @RequestMapping("/api/items/**")
    public ResponseEntity<String> proxyItems(HttpServletRequest request,
                                              @RequestBody(required = false) String body) {
        return forward(request, body, gatewayProperties.getCatalogService());
    }

    @RequestMapping("/api/inventory/**")
    public ResponseEntity<String> proxyInventory(HttpServletRequest request,
                                                  @RequestBody(required = false) String body) {
        return forward(request, body, gatewayProperties.getInventoryService());
    }

    @RequestMapping("/api/orders/**")
    public ResponseEntity<String> proxyOrders(HttpServletRequest request,
                                               @RequestBody(required = false) String body) {
        return forward(request, body, gatewayProperties.getOrderCaptureService());
    }

    @RequestMapping("/api/fulfilments/**")
    public ResponseEntity<String> proxyFulfilments(HttpServletRequest request,
                                                    @RequestBody(required = false) String body) {
        return forward(request, body, gatewayProperties.getOrderFulfilmentService());
    }

    @RequestMapping("/api/cart/**")
    public ResponseEntity<String> proxyCart(HttpServletRequest request,
                                            @RequestBody(required = false) String body) {
        return forward(request, body, gatewayProperties.getCartService());
    }

    // ── Forwarding logic ─────────────────────────────────────────

    private ResponseEntity<String> forward(HttpServletRequest request, String body,
                                            String targetBaseUrl) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String url = targetBaseUrl + path + (query != null ? "?" + query : "");

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // Skip hop-by-hop headers
            if (!headerName.equalsIgnoreCase("host")
                    && !headerName.equalsIgnoreCase("connection")
                    && !headerName.equalsIgnoreCase("content-length")) {
                headers.set(headerName, request.getHeader(headerName));
            }
        }

        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, method, entity, String.class);
    }
}
