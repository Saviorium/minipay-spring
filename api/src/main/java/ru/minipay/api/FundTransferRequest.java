package ru.minipay.api;

import java.math.BigDecimal;
import java.util.UUID;

public class FundTransferRequest extends Request{
    private final UUID fromAccId;
    private final UUID toAccId;
    private final Currency currency;
    private final BigDecimal amount;

    public FundTransferRequest(UUID fromAccId, UUID toAccId, Currency currency, BigDecimal amount) {
        super(RequestType.FundTransfer);
        this.fromAccId = fromAccId;
        this.toAccId = toAccId;
        this.currency = currency;
        this.amount = amount;
    }

    private FundTransferRequest() {
        super(RequestType.FundTransfer);
        this.fromAccId = null;
        this.toAccId = null;
        this.currency = null;
        this.amount = null;
    }

    public UUID getFromAccId() {
        return fromAccId;
    }

    public UUID getToAccId() {
        return toAccId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
