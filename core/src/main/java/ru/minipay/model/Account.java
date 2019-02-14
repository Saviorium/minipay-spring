package ru.minipay.model;

import ru.minipay.api.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Account{
    private final UUID id;
    private final Currency currency;
    private final Instant created;
    private long lastChanged;
    private final User user;
    private BigDecimal balance;

    public Account(User user, Currency currency) {
        this.user = user;
        this.currency = currency;
        this.id = UUID.randomUUID();
        this.created = Instant.now();
        this.lastChanged = 0;
        this.balance = new BigDecimal(0);
    }

    public Account(UUID id, Currency currency, Instant created, long lastChanged, User user, BigDecimal balance) {
        this.id = id;
        this.currency = currency;
        this.created = created;
        this.lastChanged = lastChanged;
        this.user = user;
        this.balance = balance;
    }

    public Account(Account account) {
        this.id = account.id;
        this.currency = account.currency;
        this.created = account.created;
        this.lastChanged = account.lastChanged;
        this.user = account.user;
        this.balance = account.balance;
    }

    private Account() { //for jackson deserialization
        this.user = null;
        this.currency = null;
        this.id = null;
        this.created = null;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreated() {
        return created;
    }

    public User getUser() {
        return user;
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

    public void updateLastChanged() {
        lastChanged++;
    }

    public long getLastChanged() {
        return lastChanged;
    }

    public boolean isChangedAfter(Account acc) {
        return this.lastChanged > acc.lastChanged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Аккаунт " + id + ", баланс = " + balance + ' ' + currency + "\n" +
                "Пользователь " + user;
    }
}
