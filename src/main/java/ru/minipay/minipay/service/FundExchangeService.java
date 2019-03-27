package ru.minipay.minipay.service;

import org.springframework.stereotype.Service;
import ru.minipay.minipay.api.Currency;

import java.math.BigDecimal;

public interface FundExchangeService {
    BigDecimal exchange(BigDecimal amount, Currency from, Currency to);
}
