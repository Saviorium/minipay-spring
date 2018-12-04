package ru.minipay.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.minipay.model.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class FundExchangeLocalTest {
    private Map<Currency, BigDecimal> exchangeRate;
    private FundExchangeService exchangeService;

    @Before
    public void setUp() {
        exchangeRate = new HashMap<>();
        exchangeRate.put(Currency.RUB, BigDecimal.ONE);
        exchangeRate.put(Currency.USD, new BigDecimal("65.6"));
        exchangeRate.put(Currency.EUR, new BigDecimal("74.8"));

        exchangeService = new FundExchangeServiceLocalImpl(exchangeRate);
    }

    @Test
    public void testExchangeCorrect() {
        BigDecimal value = exchangeService.exchange(new BigDecimal("131.2"), Currency.RUB, Currency.USD);
        BigDecimal expected = BigDecimal.valueOf(2L);
        Assert.assertEquals(0, expected.compareTo(value));

        value = exchangeService.exchange(new BigDecimal("100"), Currency.USD, Currency.EUR);
        expected = new BigDecimal("87.7");
        Assert.assertEquals(0, expected.compareTo(value.setScale(2, RoundingMode.HALF_UP)));
    }
}