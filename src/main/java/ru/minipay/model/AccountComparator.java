package ru.minipay.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Map;

public class AccountComparator implements Comparator<Account> {

    Map<Currency, BigDecimal> exchangeRate;

    public AccountComparator(Map<Currency, BigDecimal> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public int compare(Account account1, Account account2) {
        BigDecimal balance2 = account2.getBalance();
        if(account1.getCurrency() != account2.getCurrency()) {
            balance2 = convert(balance2, account1.getCurrency(), account2.getCurrency());
        }
        return account1.getBalance().subtract(balance2).signum();
    }

    private BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        amount = amount.divide(exchangeRate.get(from), new MathContext(2, RoundingMode.HALF_UP));
        amount = amount.multiply(exchangeRate.get(to));
        return amount;
    }
}
