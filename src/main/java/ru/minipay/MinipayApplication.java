package ru.minipay;

import ru.minipay.dao.AccountDao;
import ru.minipay.model.Account;
import ru.minipay.model.Currency;
import ru.minipay.model.User;
import ru.minipay.service.FundTransferService;

import java.math.BigDecimal;

public class MinipayApplication {
    private AccountDao accountDao;
    private FundTransferService fundTransferService;

    public MinipayApplication(AccountDao accountDao, FundTransferService fundTransferService) {
        this.accountDao = accountDao;
        this.fundTransferService = fundTransferService;
    }

    public Account createAccount(User user, Currency currency, BigDecimal initBalance) {
        Account account = new Account(user, currency);
        account.setBalance(initBalance);
        accountDao.insert(account);
        return account;
    }

    public void makeTransfer(Account from, Account to, Currency currency, BigDecimal amount) {
        fundTransferService.makeTransfer(from, to, currency, amount);
    }
}
