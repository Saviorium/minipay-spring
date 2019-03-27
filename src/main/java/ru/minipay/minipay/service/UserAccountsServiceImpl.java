package ru.minipay.minipay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.minipay.minipay.dao.AccountDao;
import ru.minipay.minipay.exceptions.DataAccessException;
import ru.minipay.minipay.model.Account;
import ru.minipay.minipay.api.Currency;
import ru.minipay.minipay.model.User;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserAccountsServiceImpl implements UserAccountsService {
    @Autowired
    private final AccountDao dao;

    public UserAccountsServiceImpl(AccountDao dao) {
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
