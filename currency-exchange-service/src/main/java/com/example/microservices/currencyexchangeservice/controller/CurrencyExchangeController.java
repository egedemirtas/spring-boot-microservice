package com.example.microservices.currencyexchangeservice.controller;

import com.example.microservices.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.example.microservices.currencyexchangeservice.service.ICurrencyExchangeService;
import com.example.microservices.currencyexchangeservice.validator.CurrencyExchangeValidator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/currency-exchange")
public class CurrencyExchangeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ICurrencyExchangeService currencyExchangeService;

    @GetMapping(path = "/{source}-{target}")
    public CurrencyExchangeDto getExchangeRate(@PathVariable String source, @PathVariable String target) {
        LOGGER.info("Get exchange rate called from {} to {}", source, target);

        CurrencyExchangeDto currencyExchangeDto = currencyExchangeService.getCurrencyExchangeBySourceAndTarget(source
                , target);

        CurrencyExchangeValidator.validateCurrencyExchangeDto(currencyExchangeDto, source, target);

        currencyExchangeDto.setEnvironment(environment.getProperty("local.server.port"));
        return currencyExchangeDto;
    }

    @PostMapping
    public ResponseEntity<CurrencyExchangeDto> postExchangeRate(@Valid @RequestBody CurrencyExchangeDto currencyExchangeDto) {
        CurrencyExchangeDto savedCurrencyExchangeDto =
                currencyExchangeService.saveCurrencyExchange(currencyExchangeDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .port(environment.getProperty("local.server.port"))
                .buildAndExpand(savedCurrencyExchangeDto.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
