package ru.minipay.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.minipay.MinipayApplication;
import ru.minipay.MinipayApplicationFactory;
import ru.minipay.model.Account;
import ru.minipay.model.FundTransferRequest;
import ru.minipay.model.FundTransferResult;

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

    private static String process(String requestStr) throws JsonProcessingException {
        FundTransferResult result;
        try {
            FundTransferRequest request = jsonParser.readValue(requestStr, FundTransferRequest.class);
            result = application.makeTransfer(request.getFromAccId(), request.getToAccId(), request.getCurrency(), request.getAmount());
        } catch (IOException e) {
            result = new FundTransferResult(false, e.getMessage());
        }
        return jsonParser.writeValueAsString(result);
    }
}
