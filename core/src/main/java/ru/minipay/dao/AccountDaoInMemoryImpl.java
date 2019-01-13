package ru.minipay.dao;

import ru.minipay.model.Account;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountDaoInMemoryImpl implements AccountDao{
    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void insert(Account account) {
        accounts.put(account.getId(), account);
    }

    @Override
    public Account getById(UUID id) {
        return accounts.get(id);
    }
}
