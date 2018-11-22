package ru.minipay.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Account implements Comparable<Account>{
    UUID id;
    Currency currency;
    LocalDateTime created;
    BigDecimal balance;
    User user;

    public Account(User user, Currency currency) {
        this.user = user;
        this.currency = currency;
        id = UUID.randomUUID();
        created = LocalDateTime.now();
        balance = new BigDecimal(0);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setBalance(long balance) {
        this.balance = BigDecimal.valueOf(balance);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void addMoney(BigDecimal ammount) {

    }

    public void addMoney(long ammount, Currency currency) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Account o) {
        return balance.subtract(o.balance).signum();
    }
}
