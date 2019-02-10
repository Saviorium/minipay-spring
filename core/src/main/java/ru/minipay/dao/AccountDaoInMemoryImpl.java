package ru.minipay.dao;

import ru.minipay.model.Account;

import java.util.ConcurrentModificationException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

public class AccountDaoInMemoryImpl implements AccountDao {
    private final ConcurrentMap<UUID, AtomicReference<Account>> accounts = new ConcurrentHashMap<>();

    @Override
    public void insert(Account newAcc) {
        UUID id = newAcc.getId();
        AtomicReference<Account> accountRef = accounts.putIfAbsent(id, new AtomicReference<>(newAcc));
        if(accountRef != null) {
            Account oldAcc = accountRef.get();
            if(oldAcc.isChangedAfter(newAcc)) {
                throw new ConcurrentModificationException("Error: account update collision");
            }
            newAcc.updateLastChanged();
            if(!accountRef.compareAndSet(oldAcc, newAcc)) {
                throw new ConcurrentModificationException("Error: account update collision");
            }
        }
    }

    @Override
    public Account getById(UUID id) {
        if(accounts.containsKey(id)) {
            return new Account(accounts.get(id).get());
        } else {
            return null;
        }
    }
}
