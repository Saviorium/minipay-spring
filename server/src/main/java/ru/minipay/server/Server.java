package ru.minipay.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.minipay.MinipayApplication;
import ru.minipay.MinipayApplicationFactory;
import ru.minipay.api.*;
import ru.minipay.model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final MinipayApplication application =
            MinipayApplicationFactory.getInstance().createApplication();
    private final ObjectMapper jsonParser = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private Thread thread;
    private static final int port = 12345;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public Server() {
        Account acc1 = application.createTestAccount();
        Account acc2 = application.createTestAccount();
        System.out.println(acc1 + "\n\r" + acc2);
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while(true) {
                try (Socket connection = server.accept();
                     PrintWriter out =
                             new PrintWriter(connection.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(connection.getInputStream()))
                ) {
                    String request = in.readLine();
                    System.out.println("Got request: " + request);
                    String result = process(request);
                    System.out.println("Sending response: " + result);
                    out.write(result);
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String process(String requestStr) {
        String result;
        try {
            Request request = jsonParser.readValue(requestStr, Request.class);
            Response response;
            if(request instanceof FundTransferRequest) {
                FundTransferRequest transferRequest = jsonParser.readValue(requestStr, FundTransferRequest.class);
                response = application.makeTransfer(
                        transferRequest.getFromAccId(), transferRequest.getToAccId(),
                        transferRequest.getCurrency(), transferRequest.getAmount());
            } else if(request instanceof CreateAccountRequest) {
                CreateAccountRequest createAccountRequest = jsonParser.readValue(requestStr, CreateAccountRequest.class);
                Account acc = application.createTestAccount(); //TODO: create real accounts here
                response = new CreateAccountResponse(true, acc.getId(), "");
            } else {
                response = new ErrorResponse(false, "Unsupported request type:" + request.getClass().getName());
            }
            result = jsonParser.writeValueAsString(response);
        } catch (IOException e) {
            result = e.getMessage();
        }
        return result;
    }
}
