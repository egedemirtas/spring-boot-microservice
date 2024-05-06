package com.example.microservices.currencyexchangeservice.validator;

import com.example.microservices.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.example.microservices.currencyexchangeservice.exception.ExchangeNotFoundException;

public class CurrencyExchangeValidator {
    public static void validateCurrencyExchangeDto(CurrencyExchangeDto currencyExchangeDto, String source,
                                                   String target) {
        if (currencyExchangeDto == null) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("From: ").append(source).append(", to:").append(target);
            throw new ExchangeNotFoundException(errorMessage.toString());
        }
    }
}
