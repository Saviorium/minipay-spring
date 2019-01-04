package ru.minipay.api;

import java.util.UUID;

public class CreateAccountResponse implements Response{
    private final boolean success;
    private final UUID uuid;
    private final String message;

    public CreateAccountResponse(boolean success, UUID uuid, String message) {
        this.success = success;
        this.uuid = uuid;
        this.message = message;
    }

    public CreateAccountResponse() {
        this.success = false;
        this.uuid = null;
        this.message = null;
    }

    public boolean isSuccess() {
        return success;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CreateAccountResponse{" +
                "success=" + success +
                ", uuid=" + uuid +
                ", message='" + message + '\'' +
                '}';
    }
}
