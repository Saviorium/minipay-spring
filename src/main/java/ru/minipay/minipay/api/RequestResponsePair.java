package ru.minipay.minipay.api;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RequestResponsePair {
    private final Request request;
    private final Future<Response> response;

    public RequestResponsePair(Request request, Future<Response> response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() throws ExecutionException, InterruptedException {
        return response.get();
    }
}
