package ru.minipay.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.minipay.service.FundTransferResult;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket server = new ServerSocket(port)) {
            ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule());
            while(true) {
                try (Socket connection = server.accept();
                     PrintWriter out =
                             new PrintWriter(connection.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(connection.getInputStream()))
                     ) {
                    String request = in.readLine();
                    FundTransferResult result;
                    if(request.contains("Hello")) {
                        result = new FundTransferResult(true);
                    } else {
                        result = new FundTransferResult(false, "Request must be \"Hello\"");
                    }
                    objectMapper.writeValue(out, result);
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
