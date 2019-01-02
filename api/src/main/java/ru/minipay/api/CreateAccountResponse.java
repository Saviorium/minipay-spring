package ru.minipay.api;

import java.util.UUID;

public class CreateAccountResponse {
    private final boolean success;
    private final UUID uuid;
    private final String message;

    public CreateAccountResponse(boolean success, UUID uuid, String message) {
        this.success = success;
        this.uuid = uuid;
        this.message = message;
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
}
