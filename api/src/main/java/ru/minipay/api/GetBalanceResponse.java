package ru.minipay.api;

import java.math.BigDecimal;

public class GetBalanceResponse implements Response {
    private final BigDecimal balance;
    private final Currency currency;

    public GetBalanceResponse(BigDecimal balance, Currency currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public GetBalanceResponse() {
        this.balance = null;
        this.currency = null;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }
}
