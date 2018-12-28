package ru.minipay.service;

import ru.minipay.model.Currency;

import java.math.BigDecimal;

public interface FundExchangeService {
    BigDecimal exchange(BigDecimal amount, Currency from, Currency to);
}
