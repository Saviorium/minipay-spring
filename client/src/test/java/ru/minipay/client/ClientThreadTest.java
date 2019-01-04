package ru.minipay.client;

import org.junit.Assert;
import org.junit.Test;
import ru.minipay.api.*;

import java.util.List;

public class ClientThreadTest {
    @Test
    public void TestClientThreadSingleRequest() {
        Request request = new CreateAccountRequest();
        ClientThread clientThread = new ClientThread("127.0.0.1", 12345);
        clientThread.addRequest(request);
        clientThread.sendRequests();
        List<RequestResponsePair> results = clientThread.getResults();
        CreateAccountResponse response = (CreateAccountResponse) results.get(0).getResponse();
        Assert.assertTrue(response.isSuccess());
        System.out.println(response);
    }
}
