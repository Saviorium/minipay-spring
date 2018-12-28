package ru.minipay.service;

import ru.minipay.dao.AccountDao;
import ru.minipay.model.Account;
import ru.minipay.model.Currency;
import ru.minipay.model.User;

import java.math.BigDecimal;

public class UserAccountsService {
    private final AccountDao dao;

    public UserAccountsService(AccountDao dao) {
        this.dao = dao;
    }

    public Account createAccount(User user, Currency currency, BigDecimal initBalance) {
        Account account = new Account(user, currency);
        account.setBalance(initBalance);
        dao.insert(account);
        return account;
    }
}
