package ru.minipay.api;

public class Request {
    private final RequestType type;

    public Request(RequestType type) {
        this.type = type;
    }

    private Request() {
        this.type = null;
    }

    public RequestType getType() {
        return type;
    }
}
