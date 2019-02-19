package ru.minipay.model;

import ru.minipay.api.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Transaction {
    private final UUID fromId;
    private final UUID toId;
    private final Currency currency;
    private final BigDecimal amount;
    private final Instant timestamp;

    public Transaction(UUID fromId, UUID toId, Currency currency, BigDecimal amount, Instant timestamp) {
        this.fromId = fromId;
        this.toId = toId;
        this.currency = currency;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public UUID getFromId() {
        return fromId;
    }

    public UUID getToId() {
        return toId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
