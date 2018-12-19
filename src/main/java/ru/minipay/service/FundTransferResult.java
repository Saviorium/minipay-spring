package ru.minipay.service;

public class FundTransferResult {
    private final boolean success;
    private final String message;

    public FundTransferResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public FundTransferResult(boolean success) {
        this(success, "");
    }

    public FundTransferResult() {
        this(true, "");
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
