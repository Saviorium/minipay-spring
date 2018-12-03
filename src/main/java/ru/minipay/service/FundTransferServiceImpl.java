package ru.minipay.service;

import ru.minipay.dao.AccountDao;
import ru.minipay.model.Account;
import ru.minipay.model.Currency;

import java.math.BigDecimal;

public class FundTransferServiceImpl implements FundTransferService{
    private final AccountDao dao;

    public FundTransferServiceImpl(AccountDao dao) {
        this.dao = dao;
    }

    @Override
    public void makeTransfer(Account from, Account to, Currency currency, BigDecimal amount) {
        //TODO: what if dao returns null?
        from = dao.getById(from.getId());
        to = dao.getById(to.getId());
        //TODO: currency handling
        from.setBalance(from.getBalance().subtract(amount));
        dao.insert(from);

        to.setBalance(to.getBalance().add(amount));
        dao.insert(to);
    }
}
