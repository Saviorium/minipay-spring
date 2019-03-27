package ru.minipay.minipay.service;

import org.springframework.stereotype.Service;
import ru.minipay.minipay.api.Currency;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class FundExchangeServiceLocalImpl implements FundExchangeService {
    private final Map<Currency, BigDecimal> exchangeRate;
    private static final int SCALE = 2;

    public FundExchangeServiceLocalImpl(Map<Currency, BigDecimal> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public BigDecimal exchange(BigDecimal amount, Currency from, Currency to) {
        return amount.multiply(exchangeRate.get(from))
                     .divide(exchangeRate.get(to), SCALE, java.math.RoundingMode.HALF_UP);
    }
}
