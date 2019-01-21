package ru.minipay.dao;

import ru.minipay.model.Account;

import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountDaoInMemoryImpl implements AccountDao {
    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public synchronized void insert(Account account) {
        UUID id = account.getId();
        if(accounts.containsKey(id)) {
            Account oldAcc = accounts.get(id);
            if(oldAcc.isChangedAfter(account)) {
                throw new ConcurrentModificationException("Error: account update collision");
            }
        }
        account.updateLastChanged();
        accounts.put(account.getId(), account);
    }

    @Override
    public Account getById(UUID id) {
        if(accounts.containsKey(id)) {
            return new Account(accounts.get(id));
        } else {
            return null;
        }
    }
}
