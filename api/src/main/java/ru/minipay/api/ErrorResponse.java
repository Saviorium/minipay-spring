package ru.minipay.api;

public class ErrorResponse implements Response {
    private final boolean success;
    private final String message;

    public ErrorResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
