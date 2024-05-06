package com.example.microservices.currencyexchangeservice.controller.resilience;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BulkheadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/bulkhead")
    @RateLimiter(name = "bulkhead") // defined in properties
    public String sampleApi() {
        LOGGER.info("Req received");
        return "ege";
    }
}
