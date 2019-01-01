package ru.minipay.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.minipay.MinipayApplication;
import ru.minipay.MinipayApplicationFactory;
import ru.minipay.api.CreateAccountRequest;
import ru.minipay.api.FundTransferRequest;
import ru.minipay.api.FundTransferResult;
import ru.minipay.api.Request;
import ru.minipay.model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final MinipayApplication application =
            MinipayApplicationFactory.getInstance().createApplication();
    private static final ObjectMapper jsonParser = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static void main(String[] args) {
        int port = 12345;
        Account acc1 = application.createTestAccount();
        Account acc2 = application.createTestAccount();
        System.out.println(acc1 + "\n\r" + acc2);
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (ServerSocket server = new ServerSocket(port)) {

            while(true) {
                try (Socket connection = server.accept();
                     PrintWriter out =
                             new PrintWriter(connection.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(connection.getInputStream()))
                     ) {
                    String request = in.readLine();
                    String result = process(request);
                    out.write(result);
                    out.flush();
                }
                System.out.println(acc1 + "\n\r" + acc2);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String process(String requestStr) {
        String result;
        try {
            Request request = jsonParser.readValue(requestStr, Request.class);
            if(request instanceof FundTransferRequest) {
                FundTransferRequest transferRequest = jsonParser.readValue(requestStr, FundTransferRequest.class);
                FundTransferResult transferResult = application.makeTransfer(
                        transferRequest.getFromAccId(), transferRequest.getToAccId(),
                        transferRequest.getCurrency(), transferRequest.getAmount()
                );
                result = jsonParser.writeValueAsString(transferResult);
            } else if(request instanceof CreateAccountRequest) {
                CreateAccountRequest createAccountRequest = jsonParser.readValue(requestStr, CreateAccountRequest.class);
                result = application.createTestAccount().getId().toString();
            } else {
                result = "Unsupported request type:" + request.getClass().getName();
            }
        } catch (IOException e) {
            result = e.getMessage();
        }
        return result;
    }
}
