package ru.minipay.service;

import ru.minipay.model.Currency;

import java.math.BigDecimal;
import java.util.Map;

public class FundExchangeServiceLocalImpl implements FundExchangeService {
    private final Map<Currency, BigDecimal> exchangeRate;

    public FundExchangeServiceLocalImpl(Map<Currency, BigDecimal> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public BigDecimal exchange(BigDecimal amount, Currency from, Currency to) {
        amount = amount.multiply(exchangeRate.get(from));
        int scale = 2;
        amount = amount.divide(exchangeRate.get(to), scale, java.math.RoundingMode.HALF_UP);
        return amount;
    }
}
