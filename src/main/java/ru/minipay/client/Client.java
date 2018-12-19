package ru.minipay.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.minipay.service.FundTransferResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java ru.minipay.client.Client <host name> <port number>");
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
            String userInput;
            userInput = stdIn.readLine();
            out.println(userInput);
            String response = in.readLine();
            System.out.println("Server: " + response);
            ObjectMapper objectMapper = new ObjectMapper();
            FundTransferResult resultObj = objectMapper.readValue(response, FundTransferResult.class);
            System.out.println("Got result object: " + resultObj);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
