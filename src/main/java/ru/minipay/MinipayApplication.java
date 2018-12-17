package ru.minipay;

import ru.minipay.model.Account;
import ru.minipay.model.Currency;
import ru.minipay.model.User;
import ru.minipay.service.FundTransferResult;
import ru.minipay.service.FundTransferService;
import ru.minipay.service.UserAccountsService;

import java.math.BigDecimal;
import java.util.UUID;

public class MinipayApplication {
    private final FundTransferService fundTransferService;
    private final UserAccountsService userAccountsService;

    public MinipayApplication(UserAccountsService userAccountsService, FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
        this.userAccountsService = userAccountsService;
    }

    public Account createAccount(User user, Currency currency, BigDecimal initBalance) {
        return this.userAccountsService.createAccount(user, currency, initBalance);
    }

    public FundTransferResult makeTransfer(UUID fromAccId, UUID toAccId, Currency currency, BigDecimal amount) {
        return fundTransferService.makeTransfer(fromAccId, toAccId, currency, amount);
    }
}
