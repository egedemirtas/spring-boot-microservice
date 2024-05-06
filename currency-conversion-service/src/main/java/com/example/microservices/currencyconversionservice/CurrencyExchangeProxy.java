package com.example.microservices.currencyconversionservice;

import com.example.microservices.currencyconversionservice.dto.CurrencyConversionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// we use the application name of the service we want to use
// @FeignClient(name = "currency-exchange-service", url="localhost:8000")
@FeignClient(name = "currency-exchange-service") // removed url to enable load balancing
public interface CurrencyExchangeProxy {
    @GetMapping("/currency-exchange/{source}-{target}")
    public CurrencyConversionDto getExchangeRate(@PathVariable String source, @PathVariable String target);
}
