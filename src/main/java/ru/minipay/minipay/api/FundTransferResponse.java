package ru.minipay.minipay.api;

public class FundTransferResponse extends Response {

    public FundTransferResponse(boolean success, String message) {
        super(success, message);
    }

    public FundTransferResponse(boolean success) {
        this(success, "");
    }

    private FundTransferResponse() {
        this(true, "");
    }
}
