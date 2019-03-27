package ru.minipay.minipay.service;

import org.springframework.stereotype.Service;
import ru.minipay.minipay.api.Currency;
import ru.minipay.minipay.exceptions.DataAccessException;
import ru.minipay.minipay.model.Account;
import ru.minipay.minipay.model.User;

import java.math.BigDecimal;
import java.util.UUID;

public interface UserAccountsService {
    Account createAccount(User user, Currency currency, BigDecimal initBalance) throws DataAccessException;

    Account getAccount(UUID accId) throws DataAccessException;
}
