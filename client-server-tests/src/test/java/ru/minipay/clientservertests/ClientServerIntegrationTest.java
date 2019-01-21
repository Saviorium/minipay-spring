package ru.minipay.clientservertests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.minipay.api.*;
import ru.minipay.api.Currency;
import ru.minipay.client.ClientMultiThread;
import ru.minipay.server.Server;
import ru.minipay.server.ServerMultiThread;
import ru.minipay.server.ServerThreadPool;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class ClientServerIntegrationTest {
    private static final Server server = new ServerThreadPool();
    private static final String serverIP = "127.0.0.1";

    @BeforeClass
    public static void setUp() {
        server.start();
    }

    @Test
    public void TestClientThreadSingleRequest() throws InterruptedException, ExecutionException {
        Request request = new CreateAccountRequest();
        ClientMultiThread clientThread = new ClientMultiThread(serverIP);
        Future<Response> responseFuture = clientThread.addRequest(request);
        CreateAccountResponse response = (CreateAccountResponse) responseFuture.get();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void TestClientThreadTransferRequest() throws InterruptedException, ExecutionException {
        ClientMultiThread client = new ClientMultiThread(serverIP);
        Request request = new CreateAccountRequest();
        CreateAccountResponse response;

        response = (CreateAccountResponse)client.addRequest(request).get();
        Assert.assertTrue(response.isSuccess());
        UUID acc1 = response.getUuid();

        response = (CreateAccountResponse)client.addRequest(request).get();
        Assert.assertTrue(response.isSuccess());
        UUID acc2 = response.getUuid();

        request = new FundTransferRequest(acc1, acc2, Currency.RUB, BigDecimal.ONE);
        FundTransferResponse responseTransfer = (FundTransferResponse) client.addRequest(request).get();
        Assert.assertTrue(responseTransfer.isSuccess());

        request = new GetBalanceRequest(acc1);
        GetBalanceResponse balanceResponse = (GetBalanceResponse) client.addRequest(request).get();
        Assert.assertEquals(99, balanceResponse.getBalance().intValue());

        request = new GetBalanceRequest(acc2);
        balanceResponse = (GetBalanceResponse) client.addRequest(request).get();
        Assert.assertEquals(101, balanceResponse.getBalance().intValue());
    }

    @Test
    public void TestRandom1000Requests() throws InterruptedException, ExecutionException {
        ClientMultiThread client = new ClientMultiThread(serverIP);
        //generate users
        final int usersNum = 100;
        List<UUID> users = new ArrayList<>();
        for(int i = 0; i<usersNum; i++) {
            Request request = new CreateAccountRequest();
            CreateAccountResponse responseAcc = (CreateAccountResponse) client.addRequest(request).get();
            users.add(responseAcc.getUuid());
        }
        //send funds randomly
        final int reqNum = 1000;
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        List<Future<Response>> responses = new ArrayList<>();
        long time = System.nanoTime();
        for(int i = 0; i<reqNum; i++) {
            Request request = new FundTransferRequest(
                    users.get(rnd.nextInt(usersNum)),
                    users.get(rnd.nextInt(usersNum)),
                    Currency.RUB, BigDecimal.ONE);
            responses.add(client.addRequest(request));
        }
        //wait until got all responses
        client.awaitTermination();
        time = System.nanoTime() - time;

        System.out.println("Sent " + reqNum + " requests in " + time/1000000D + " ms");
        for(int i = 0; i<reqNum; i++) {
            Assert.assertTrue(responses.get(i).isDone());
        }
    }

    @Test
    public void TestGetBalanceResponse() throws InterruptedException, ExecutionException {
        ClientMultiThread client = new ClientMultiThread(serverIP);

        Request request = new CreateAccountRequest();
        CreateAccountResponse responseAcc = (CreateAccountResponse) client.addRequest(request).get();
        UUID acc = responseAcc.getUuid();

        request = new GetBalanceRequest(acc);
        GetBalanceResponse balanceResponse = (GetBalanceResponse) client.addRequest(request).get();
        Assert.assertEquals(BigDecimal.valueOf(100L), balanceResponse.getBalance());
    }

    @Test
    public void TestFundTransferConcurrency() throws InterruptedException, ExecutionException {
        ClientMultiThread client = new ClientMultiThread(serverIP);
        final int usersNum = 5;
        final int initialBalance = 100;
        //users generation
        UUID[] users = new UUID[usersNum];
        for(int i = 0; i<usersNum; i++) {
            Request request = new CreateAccountRequest();
            CreateAccountResponse responseAcc = (CreateAccountResponse) client.addRequest(request).get();
            users[i] = responseAcc.getUuid();
        }
        //prepare counters
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        Map<UUID, Integer> expectedBalance = new HashMap<>();
        for (int i = 0; i < usersNum; i++) {
            expectedBalance.put(users[i], initialBalance);
        }
        //send requests
        final int reqNum = 1000;
        Deque<RequestResponsePair> responses = new LinkedList<>();
        for(int i = 0; i<reqNum; i++) {
            int from = rnd.nextInt(usersNum);
            int to = rnd.nextInt(usersNum);
            Request request = new FundTransferRequest(
                    users[from],
                    users[to],
                    Currency.RUB, BigDecimal.ONE);
            responses.add(new RequestResponsePair(request, client.addRequest(request)));
        }
        client.awaitTermination();
        //count results
        while(!responses.isEmpty()) {
            RequestResponsePair responsePair = responses.pop();
            FundTransferRequest request = (FundTransferRequest) responsePair.getRequest();
            if(responsePair.getResponse().isSuccess()) {
                expectedBalance.put(request.getFromAccId(), expectedBalance.get(request.getFromAccId()) - 1);
                expectedBalance.put(request.getToAccId(), expectedBalance.get(request.getToAccId()) + 1);
            }
        }
        //check results
        client = new ClientMultiThread(serverIP);
        for(int i = 0; i<usersNum; i++) {
            Request request = new GetBalanceRequest(users[i]);
            GetBalanceResponse balanceResponse = (GetBalanceResponse) client.addRequest(request).get();
            System.out.println("expected: " + expectedBalance.get(users[i]) + ", got: " + balanceResponse.getBalance().intValue());
            Assert.assertEquals(expectedBalance.get(users[i]).intValue(), balanceResponse.getBalance().intValue());
        }
    }
}
