package ru.minipay.dao;

import ru.minipay.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountDaoInMemoryImpl implements AccountDao{
    private final Map<UUID, Account> accounts = new HashMap<>();

    @Override
    public void insert(Account account) {
        accounts.put(account.getId(), account);
    }

    @Override
    public Account getById(UUID id) {
        return accounts.get(id);
    }
}
