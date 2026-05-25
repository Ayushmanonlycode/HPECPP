package com.showdown.springboot.orderfulfilmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class OrderFulfilmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderFulfilmentServiceApplication.class, args);
    }

    @RestController
    static class HealthController {
        @GetMapping("/health")
        public String health() {
            return "order-fulfilment-service running";
        }
    }
}
