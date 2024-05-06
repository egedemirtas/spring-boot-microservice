package com.example.microservices.currencyexchangeservice.mapper;

import com.example.microservices.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.example.microservices.currencyexchangeservice.entity.CurrencyExchange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ICurrencyExchangeMapper {

    @Mapping(target="id", ignore = true)
    CurrencyExchange dto2Entity(CurrencyExchangeDto currencyExchangeDto);

    CurrencyExchangeDto Entity2Dto(CurrencyExchange currencyExchange);
}
