package ru.minipay.dao;

import ru.minipay.model.Account;

import java.util.UUID;

public interface AccountDao {
    void insert(Account account);
    Account getById(UUID id);
}
