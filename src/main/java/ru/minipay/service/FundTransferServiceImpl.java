package ru.minipay.service;

import ru.minipay.dao.AccountDao;
import ru.minipay.model.Account;
import ru.minipay.model.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public class FundTransferServiceImpl implements FundTransferService{
    private final AccountDao dao;
    private final FundExchangeService exchangeService;

    public FundTransferServiceImpl(AccountDao dao, FundExchangeService exchangeService) {
        this.dao = dao;
        this.exchangeService = exchangeService;
    }

    @Override
    public void makeTransfer(UUID fromId, UUID toId, Currency currency, BigDecimal amount) {
        Account from = dao.getById(fromId);
        Account to = dao.getById(toId);
        if(from == null || to == null) {
            throw new IllegalArgumentException("User not found");
        }
        BigDecimal amountInCurrency = exchangeService.exchange(amount, currency, from.getCurrency());
        from.setBalance(from.getBalance().subtract(amountInCurrency));
        dao.insert(from);

        amountInCurrency = exchangeService.exchange(amount, currency, to.getCurrency());
        to.setBalance(to.getBalance().add(amountInCurrency));
        dao.insert(to);
    }
}
