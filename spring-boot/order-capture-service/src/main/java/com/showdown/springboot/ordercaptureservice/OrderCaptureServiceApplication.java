package com.showdown.springboot.ordercaptureservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class OrderCaptureServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderCaptureServiceApplication.class, args);
    }

    @RestController
    static class HealthController {
        @GetMapping("/health")
        public String health() {
            return "order-capture-service running";
        }
    }
}
