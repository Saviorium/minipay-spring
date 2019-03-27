package ru.minipay.minipay.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.minipay.minipay.model.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
@Primary //TODO: Proper configuration
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
