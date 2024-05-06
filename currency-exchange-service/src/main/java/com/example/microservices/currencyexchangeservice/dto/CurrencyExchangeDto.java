package com.example.microservices.currencyexchangeservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyExchangeDto extends BaseDto {
    @NotBlank(message = "Source must not be blank")
    private String source;
    @NotBlank(message = "Target must not be blank")
    private String target;

    @Min(value = 0, message = "Rate cannot be negative")
    private BigDecimal rate;
}
