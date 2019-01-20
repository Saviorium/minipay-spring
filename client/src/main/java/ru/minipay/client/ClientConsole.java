package ru.minipay.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import ru.minipay.api.*;

public class ClientConsole {
    private static final ObjectMapper jsonParser = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java java.ru.minipay.client.Client <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {
            Request request = getRequest(stdIn);
            if(request != null) {
                out.println(jsonParser.writeValueAsString(request));
                String response = in.readLine();
                System.out.println("Server: " + response);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O Exception: " +
                    e.getMessage());
            System.exit(1);
        }
    }

    private static Request getRequest(BufferedReader stdIn) throws IOException {
        System.out.println("Enter request type (1 - create account, 2 - fund transfer, 3 - get balance):");
        int userInput = Integer.parseInt(stdIn.readLine());
        switch (userInput) {
            case 1: { return getCreateAccountRequest(stdIn); }
            case 2: { return getFundTransferRequest(stdIn); }
            case 3: { return getGetBalanceRequest(stdIn); }
            default: { System.out.println("Error: unsupported request type: " + userInput); }
        }
        return null;
    }

    private static Request getGetBalanceRequest(BufferedReader stdIn) throws IOException {
        System.out.println("Enter id:");
        UUID id = UUID.fromString(stdIn.readLine());
        return new GetBalanceRequest(id);
    }

    private static Request getFundTransferRequest(BufferedReader stdIn) throws IOException {
        System.out.println("Enter id from:");
        UUID idFrom = UUID.fromString(stdIn.readLine());
        System.out.println("Enter id to:");
        UUID idTo = UUID.fromString(stdIn.readLine());
        System.out.println("Enter amount RUB:");
        BigDecimal amount = new BigDecimal(stdIn.readLine());
        return new FundTransferRequest(idFrom, idTo, Currency.RUB, amount);
    }

    private static Request getCreateAccountRequest(BufferedReader stdIn) {
        return new CreateAccountRequest();
    }
}
