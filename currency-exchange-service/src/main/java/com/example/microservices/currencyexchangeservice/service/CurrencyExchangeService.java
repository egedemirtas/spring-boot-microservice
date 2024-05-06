package com.example.microservices.currencyexchangeservice.service;

import com.example.microservices.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.example.microservices.currencyexchangeservice.entity.CurrencyExchange;
import com.example.microservices.currencyexchangeservice.mapper.ICurrencyExchangeMapper;
import com.example.microservices.currencyexchangeservice.repository.CurrencyExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyExchangeService implements ICurrencyExchangeService {
    @Autowired
    CurrencyExchangeRepository currencyExchangeRepository;
    @Autowired
    ICurrencyExchangeMapper currencyExchangeMapper;

    public CurrencyExchangeDto getCurrencyExchangeBySourceAndTarget(String source, String target) {
        Optional<CurrencyExchange> currencyExchange = currencyExchangeRepository.findBySourceAndTarget(source, target);
        return currencyExchangeMapper.Entity2Dto(currencyExchange.orElse(null));
    }

    @Override
    public CurrencyExchangeDto saveCurrencyExchange(CurrencyExchangeDto currencyExchangeDto) {
        CurrencyExchange response =
                currencyExchangeRepository.save(currencyExchangeMapper.dto2Entity(currencyExchangeDto));
        return currencyExchangeMapper.Entity2Dto(response);
    }
}
