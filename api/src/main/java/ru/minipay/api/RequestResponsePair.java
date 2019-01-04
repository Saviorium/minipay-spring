package ru.minipay.api;

public class RequestResponsePair {
    private final Request request;
    private final Response response;

    public RequestResponsePair(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
