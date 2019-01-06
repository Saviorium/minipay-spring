package ru.minipay.client;

import org.junit.Assert;
import org.junit.Test;
import ru.minipay.api.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ClientMultiThreadTest {
    @Test
    public void TestClientThreadSingleRequest() throws InterruptedException {
        Request request = new CreateAccountRequest();
        ClientMultiThread clientThread = new ClientMultiThread("127.0.0.1", 12345);
        clientThread.addRequest(request);
        RequestResponsePair result = clientThread.getNextResult();
        CreateAccountResponse response = (CreateAccountResponse) result.getResponse();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void TestClientThreadTransferRequest() throws InterruptedException {
        ClientMultiThread clientThread = new ClientMultiThread("127.0.0.1", 12345);
        Request request = new CreateAccountRequest();
        clientThread.addRequest(request);
        clientThread.addRequest(request);
        CreateAccountResponse responseAcc = (CreateAccountResponse) clientThread.getNextResult().getResponse();
        Assert.assertTrue(responseAcc.isSuccess());
        UUID acc1 = responseAcc.getUuid();
        responseAcc = (CreateAccountResponse) clientThread.getNextResult().getResponse();
        Assert.assertTrue(responseAcc.isSuccess());
        UUID acc2 = responseAcc.getUuid();

        request = new FundTransferRequest(acc1, acc2, Currency.RUB, BigDecimal.ONE);
        clientThread.addRequest(request);
        FundTransferResponse responseTransfer = (FundTransferResponse) clientThread.getNextResult().getResponse();
        Assert.assertTrue(responseTransfer.isSuccess());
    }

    @Test
    public void TestRandom1000Requests() throws InterruptedException {
        ClientMultiThread clientThread = new ClientMultiThread("127.0.0.1", 12345);
        //generate users
        final int usersNum = 100;
        List<UUID> users = new ArrayList<>();
        for(int i = 0; i<usersNum; i++) {
            Request request = new CreateAccountRequest();
            clientThread.addRequest(request);
            CreateAccountResponse responseAcc = (CreateAccountResponse) clientThread.getNextResult().getResponse();
            users.add(responseAcc.getUuid());
        }
        //send funds randomly
        final int reqNum = 1000;
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        long time = System.nanoTime();
        for(int i = 0; i<reqNum; i++) {
            Request request = new FundTransferRequest(
                    users.get(rnd.nextInt(usersNum)),
                    users.get(rnd.nextInt(usersNum)),
                    Currency.RUB, BigDecimal.ONE);
            clientThread.addRequest(request);
        }
        //wait until got all responses
        clientThread.awaitTermination();
        time = System.nanoTime() - time;

        System.out.println("Sent " + reqNum + " requests in " + time/1000000D + " ms");
        for(int i = 0; i<reqNum; i++) {
            FundTransferResponse responseTransfer = (FundTransferResponse) clientThread.getNextResult().getResponse();
            Assert.assertTrue(responseTransfer.isSuccess());
        }
    }
}
