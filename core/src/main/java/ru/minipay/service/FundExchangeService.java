package ru.minipay.service;

import ru.minipay.api.Currency;

import java.math.BigDecimal;

public interface FundExchangeService {
    BigDecimal exchange(BigDecimal amount, Currency from, Currency to);
}
