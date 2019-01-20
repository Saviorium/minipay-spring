package ru.minipay.api;

import java.util.UUID;

public class GetBalanceRequest extends Request {
    private final UUID accId;

    public GetBalanceRequest(UUID accId) {
        this.accId = accId;
    }

    private GetBalanceRequest() {
        accId = null;
    }

    public UUID getAccId() {
        return accId;
    }
}
