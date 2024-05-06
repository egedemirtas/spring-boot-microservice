package com.example.microservices.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        route -> route.path("/currency-exchange/**").
                                uri("lb://currency-exchange-service"))
                .route(
                        route -> route.path("/currency-conversion/**").
                                uri("lb://currency-conversion-service")
                ).build();
    }

}
