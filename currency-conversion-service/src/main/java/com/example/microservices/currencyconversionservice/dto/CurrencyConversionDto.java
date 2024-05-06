package com.example.microservices.currencyconversionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionDto {
    private String source;
    private String target;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal calculatedAmount;
    private String environment;
}