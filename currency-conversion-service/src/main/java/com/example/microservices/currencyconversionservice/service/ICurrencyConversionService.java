package com.example.microservices.currencyconversionservice.service;

import com.example.microservices.currencyconversionservice.dto.CurrencyConversionDto;

import java.math.BigDecimal;

public interface ICurrencyConversionService {

    CurrencyConversionDto calculatedConversionAmount(String source, String target, BigDecimal amount);
}
