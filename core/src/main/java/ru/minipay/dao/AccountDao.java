package ru.minipay.dao;

import ru.minipay.model.Account;

import java.util.List;
import java.util.UUID;

public interface AccountDao {
    void insert(Account account);
    void insert(List<Account> accounts);
    Account getById(UUID id);
}
