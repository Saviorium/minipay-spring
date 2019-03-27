package ru.minipay.minipay.api;

import java.math.BigDecimal;

public class GetBalanceResponse extends Response {
    private final BigDecimal balance;
    private final Currency currency;

    public GetBalanceResponse(BigDecimal balance, Currency currency) {
        super(true, "");
        this.balance = balance;
        this.currency = currency;
    }

    GetBalanceResponse() {
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
