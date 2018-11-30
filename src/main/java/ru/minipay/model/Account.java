package ru.minipay.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Account{
    private final UUID id;
    private final Currency currency;
    private final Instant created;
    private final User user;
    private BigDecimal balance;

    public Account(User user, Currency currency) {
        this.user = user;
        this.currency = currency;
        this.id = UUID.randomUUID();
        this.created = Instant.now();
        this.balance = new BigDecimal(0);
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
    public String toString() {
        return "Аккаунт " + id + ", баланс = " + balance + ' ' + currency + "\n" +
                "Пользователь " + user;
    }
}
