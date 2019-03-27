package ru.minipay.minipay.service;

import org.springframework.stereotype.Service;
import ru.minipay.minipay.api.Currency;
import ru.minipay.minipay.api.FundTransferResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface FundTransferService {
    FundTransferResponse makeTransfer(UUID fromId, UUID toId, Currency currency, BigDecimal amount);
}
