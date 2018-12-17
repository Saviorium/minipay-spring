package ru.minipay.service;

import ru.minipay.model.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public interface FundTransferService {
    FundTransferResult makeTransfer(UUID fromId, UUID toId, Currency currency, BigDecimal amount);
}
