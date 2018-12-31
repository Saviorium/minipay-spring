package ru.minipay.api;

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

    private FundTransferResult() {
        this(true, "");
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return success?"Success":"Failure" + "! Message: " + message;
    }
}
