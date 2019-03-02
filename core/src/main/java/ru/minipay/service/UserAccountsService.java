package ru.minipay.service;

import ru.minipay.dao.AccountDao;
import ru.minipay.exceptions.DataAccessException;
import ru.minipay.model.Account;
import ru.minipay.api.Currency;
import ru.minipay.model.User;

import java.math.BigDecimal;
import java.util.UUID;

public class UserAccountsService {
    private final AccountDao dao;

    public UserAccountsService(AccountDao dao) {
        this.dao = dao;
    }

    public Account createAccount(User user, Currency currency, BigDecimal initBalance) throws DataAccessException {
        Account account = new Account(user, currency);
        account.setBalance(initBalance);
        dao.insert(account);
        return dao.getById(account.getId());
    }

    public Account getAccount(UUID accId) throws DataAccessException {
        return dao.getById(accId);
    }
}
