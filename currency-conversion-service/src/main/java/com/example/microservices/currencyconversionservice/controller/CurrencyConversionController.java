package com.example.microservices.currencyconversionservice.controller;

import com.example.microservices.currencyconversionservice.CurrencyExchangeProxy;
import com.example.microservices.currencyconversionservice.dto.CurrencyConversionDto;
import com.example.microservices.currencyconversionservice.service.ICurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("currency-conversion")
public class CurrencyConversionController {
    @Autowired
    ICurrencyConversionService currencyConversionService;

    @GetMapping("{source}-{target}/{amount}")
    public CurrencyConversionDto getConversedAmount(@PathVariable String source, @PathVariable String target,
                                                    @PathVariable BigDecimal amount) {

        return currencyConversionService.calculatedConversionAmount(source, target, amount);
    }
}
