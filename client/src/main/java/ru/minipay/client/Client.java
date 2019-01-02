package ru.minipay.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.minipay.api.RequestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
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
            RequestType requestType = getRequestType(stdIn);

            if (requestType == RequestType.CreateAccount) {
                out.println("{\"type\":\"CreateAccount\"}");
            } else if (requestType == RequestType.FundTransfer) {
                StringBuilder requestStr = new StringBuilder("{\"type\":\"FundTransfer\",\"fromAccId\":\"");
                System.out.println("Enter id from:");
                requestStr.append(stdIn.readLine()).append("\",\"toAccId\":\"");
                System.out.println("Enter id to:");
                requestStr.append(stdIn.readLine()).append("\",\"currency\":\"RUB\",\"amount\":");
                System.out.println("Enter amount RUB:");
                requestStr.append(stdIn.readLine()).append("}");
                System.out.println(requestStr);
                out.println(requestStr);
            }
            String response = in.readLine();
            System.out.println("Server: " + response);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    private static RequestType getRequestType(BufferedReader stdIn) throws IOException {
        System.out.println("Enter request type (1 - create account, 2 - fund transfer):");
        int userInput = Integer.parseInt(stdIn.readLine());
        switch (userInput) {
            case 1: { return RequestType.CreateAccount; }
            case 2: { return RequestType.FundTransfer; }
            default: { System.out.println("Error: unsupported request type: " + userInput); }
        }
        return null;
    }
}
