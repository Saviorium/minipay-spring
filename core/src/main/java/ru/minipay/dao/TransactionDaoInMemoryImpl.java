package ru.minipay.dao;

import ru.minipay.model.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TransactionDaoInMemoryImpl implements TransactionDao{
    private final ConcurrentLinkedDeque<Transaction> txnList = new ConcurrentLinkedDeque<>();

    @Override
    public void insert(Transaction txn) {
        txnList.add(txn);
    }

    @Override
    public List<Transaction> getAllById(UUID id) {
        return null; //TODO: implement getAllById TransactionDaoInMemoryImpl
    }
}
