package com.example.microservices.currencyconversionservice.service;

import com.example.microservices.currencyconversionservice.CurrencyExchangeProxy;
import com.example.microservices.currencyconversionservice.dto.CurrencyConversionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConversionService implements ICurrencyConversionService {
    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;

    @Override
    public CurrencyConversionDto calculatedConversionAmount(String source, String target, BigDecimal amount) {
        CurrencyConversionDto currencyConversionDto = currencyExchangeProxy.getExchangeRate(source, target);
        currencyConversionDto.setAmount(amount);
        currencyConversionDto.setCalculatedAmount(amount.multiply(currencyConversionDto.getRate()));
        return currencyConversionDto;
    }
}
