package ru.minipay.service;

import ru.minipay.model.Account;
import ru.minipay.model.Currency;

import java.math.BigDecimal;

public class FundTransferServiceImpl implements FundTransferService{
    @Override
    public void makeTransfer(Account from, Account to, Currency currency, BigDecimal amount) {

    }
}
