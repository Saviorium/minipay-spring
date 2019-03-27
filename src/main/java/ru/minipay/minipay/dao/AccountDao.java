package ru.minipay.minipay.dao;

import ru.minipay.minipay.exceptions.DataAccessException;
import ru.minipay.minipay.model.Account;

import java.util.List;
import java.util.UUID;

public interface AccountDao {
    void insert(Account account) throws DataAccessException;
    void insert(List<Account> accounts) throws DataAccessException;
    Account getById(UUID id) throws DataAccessException;
}
