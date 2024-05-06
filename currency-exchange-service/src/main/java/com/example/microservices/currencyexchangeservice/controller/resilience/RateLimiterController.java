package com.example.microservices.currencyexchangeservice.controller.resilience;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/rate-limiter")
    @RateLimiter(name = "ratelimit") // defined in properties
    public String sampleApi() {
        LOGGER.info("Req received");
        return "ege";
    }
}
