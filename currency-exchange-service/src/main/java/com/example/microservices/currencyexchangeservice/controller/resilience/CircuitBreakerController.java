package com.example.microservices.currencyexchangeservice.controller.resilience;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CircuitBreakerController.class);

    /**
     * You can test it via postman by running performance test and calling this api repeatedly
     * You will see that after some point the api won't be called, "Req received" won't be printed
     * At this point request will be sent but circuit breaker will return an exception without calling a method
     */
    @GetMapping("/circuit")
    @CircuitBreaker(name="default", fallbackMethod = "hardCodedResponse")
    public String sampleApi() {
        LOGGER.info("Req received");
        new RestTemplate().getForEntity("http://localhost:8080/dummy", String.class);
        return "ege";
    }

    public String hardCodedResponse(Exception exception){
        return "hard coded response for fallback";
    }
}
