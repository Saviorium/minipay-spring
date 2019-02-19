package ru.minipay.dao;

import ru.minipay.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionDao {
    void insert(Transaction txn);
    List<Transaction> getAllById(UUID id);
}
