package com.example.microservices.currencyexchangeservice.service;

import com.example.microservices.currencyexchangeservice.dto.CurrencyExchangeDto;

public interface ICurrencyExchangeService {
    CurrencyExchangeDto getCurrencyExchangeBySourceAndTarget(String source, String target);

    CurrencyExchangeDto saveCurrencyExchange(CurrencyExchangeDto currencyExchangeDto);
}
