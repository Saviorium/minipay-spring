package ru.minipay.service;

import ru.minipay.model.Currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateGenerator {
    private ExchangeRateGenerator() {}

    public static Map<Currency, BigDecimal> getSampleExchangeRate() {
        Map<Currency, BigDecimal> exchangeRate = new HashMap<>();
        exchangeRate.put(Currency.RUB, BigDecimal.ONE);
        exchangeRate.put(Currency.USD, new BigDecimal("65.6"));
        exchangeRate.put(Currency.EUR, new BigDecimal("74.8"));
        return exchangeRate;
    }
}
