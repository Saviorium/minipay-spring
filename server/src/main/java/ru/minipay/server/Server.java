package ru.minipay.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.*;
import ru.minipay.MinipayApplication;
import ru.minipay.MinipayApplicationFactory;
import ru.minipay.api.*;
import ru.minipay.exceptions.DataAccessException;
import ru.minipay.model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final MinipayApplication application =
            MinipayApplicationFactory.getInstance().createPostgresApplication();
    private final ObjectMapper jsonParser = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private Thread thread;
    private static final int port = 12345;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public Server() {
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
                Socket connection = server.accept();
                handleConnection(connection);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void handleConnection(Socket conn) throws IOException {
        try (conn; PrintWriter out =
                new PrintWriter(conn.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(conn.getInputStream()))) {
            String request = in.readLine();
            LogManager.getLogger().info("Got request: " + request);
            String result = process(request);
            LogManager.getLogger().info("Sending response: " + result);
            out.write(result);
            out.flush();
        }
    }

    private String process(String requestStr) {
        String result;
        try {
            if(requestStr == null) {
                return jsonParser.writeValueAsString(new ErrorResponse(false, "Got null request"));
            }
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
                if(acc == null) {
                    response = new  CreateAccountResponse(false, null, "Cannot create account. Got null account.");
                } else {
                    response = new CreateAccountResponse(true, acc.getId(), "");
                }
            } else if(request instanceof GetBalanceRequest) {
                GetBalanceRequest getBalanceRequest = jsonParser.readValue(requestStr, GetBalanceRequest.class);
                Account acc = application.getAccount(getBalanceRequest.getAccId());
                response = new GetBalanceResponse(acc.getBalance(), acc.getCurrency());
            } else {
                response = new ErrorResponse(false, "Unsupported request type:" + request.getClass().getName());
            }
            result = jsonParser.writeValueAsString(response);
        } catch (IOException e) {
            LogManager.getLogger().error("Got IOException parsing json or making json response", e);
            result = e.getMessage();
        } catch (DataAccessException e) {
            LogManager.getLogger().error("Got DataAccessException", e);
            try {
                result = jsonParser.writeValueAsString(new ErrorResponse(false, "Error accessing the database"));
            } catch (JsonProcessingException e1) {
                result = e.getMessage();
            }
        }
        return result;
    }
}
