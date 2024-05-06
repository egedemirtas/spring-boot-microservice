package com.example.microservices.currencyexchangeservice.controller.resilience;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RetryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryController.class);

    @GetMapping("/sample")
    @Retry(name = "default") // default: indicates that it will try 3 times and at 3rd if it fails it will throw error
    public String sampleApi() {
        LOGGER.info("Req received"); // you can see that error has been thrown at 3rd try
        new RestTemplate().getForEntity("http://localhost:8080/dummy", String.class); // this endpoint doesn't exist
        // and will throw exception
        return "ege";
    }

    @GetMapping("/custom")
    @Retry(name = "custom-retry") // we configured this at application.properties
    public String customApi() {
        LOGGER.info("Req received"); // you can see that error has been thrown at 5th try
        new RestTemplate().getForEntity("http://localhost:8080/dummy", String.class);
        return "ege";
    }

    @GetMapping("/fallback")
    @Retry(name = "default", fallbackMethod = "hardCodedResponse") // need to define fallback as a method
    public String fallbackApi() {
        LOGGER.info("Req received");
        new RestTemplate().getForEntity("http://localhost:8080/dummy", String.class);
        return "ege";
    }

    // parameter type should be Throwable
    // in here Exception is used however you can create different fallback methods for different exception classes
    // that inherit from Throwable
    // right now any child exception class of Exception will return this
    public String hardCodedResponse(Exception exception){
        return "hard coded response for fallback";
    }

    @GetMapping("/wait")
    @Retry(name = "wait") // this is configured in properties
    public String waitApi() {
        LOGGER.info("Req received");
        new RestTemplate().getForEntity("http://localhost:8080/dummy", String.class);
        return "ege";
    }


    @GetMapping("/exponential")
    @Retry(name = "exponential") // this is configured in properties
    public String exponentialApi() {
        LOGGER.info("Req received");
        new RestTemplate().getForEntity("http://localhost:8080/dummy", String.class);
        return "ege";
    }
}
