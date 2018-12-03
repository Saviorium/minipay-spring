package ru.minipay;

import ru.minipay.model.Account;
import ru.minipay.model.Currency;
import ru.minipay.model.User;

import java.math.BigDecimal;

public class MinipayApplication {
    Account createAccount(User user, Currency currency, BigDecimal initBalance) {
        Account account = new Account(user, currency);
        account.setBalance(initBalance);
        return account;
    }

    void makeTransfer(Account from, Account to, Currency currency, BigDecimal amount) {

    }
}
