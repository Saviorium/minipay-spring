package ru.minipay.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Account{
    private UUID id;
    private Currency currency;
    private LocalDateTime created;
    private BigDecimal balance;
    private User user;

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
