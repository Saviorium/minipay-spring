package ru.minipay.api;

public class FundTransferResponse implements Response {
    private final boolean success;
    private final String message;

    public FundTransferResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public FundTransferResponse(boolean success) {
        this(success, "");
    }

    private FundTransferResponse() {
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
