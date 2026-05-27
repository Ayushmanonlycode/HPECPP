package com.showdown.springboot.apigateway.controller;

import com.showdown.springboot.apigateway.config.GatewayProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GatewayController.class)
public class GatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private GatewayProperties gatewayProperties;

    @BeforeEach
    void setUp() {
        when(gatewayProperties.getUserService()).thenReturn("http://user-service");
        when(gatewayProperties.getCatalogService()).thenReturn("http://catalog-service");
        when(gatewayProperties.getInventoryService()).thenReturn("http://inventory-service");
    }

    @Test
    void proxyUsers_shouldForwardToUserService() throws Exception {
        String targetUrl = "http://user-service/api/users/123";
        ResponseEntity<String> response = new ResponseEntity<>("{\"username\": \"john\"}", HttpStatus.OK);

        when(restTemplate.exchange(
                eq(targetUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        mockMvc.perform(get("/api/users/123")
                        .header("X-Test-Header", "value"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\": \"john\"}"));

        verify(restTemplate, times(1)).exchange(
                eq(targetUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void proxyCategories_shouldForwardToCatalogService() throws Exception {
        String targetUrl = "http://catalog-service/api/categories";
        ResponseEntity<String> response = new ResponseEntity<>("[]", HttpStatus.OK);

        when(restTemplate.exchange(
                eq(targetUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void proxyPostInventory_shouldForwardToInventoryService() throws Exception {
        String targetUrl = "http://inventory-service/api/inventory/EST-1/reserve";
        ResponseEntity<String> response = new ResponseEntity<>("{\"sku\":\"EST-1\"}", HttpStatus.OK);

        when(restTemplate.exchange(
                eq(targetUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        mockMvc.perform(post("/api/inventory/EST-1/reserve")
                        .content("{\"quantity\": 5}")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
