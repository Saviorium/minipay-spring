package ru.minipay.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.minipay.api.Request;
import ru.minipay.api.RequestResponsePair;
import ru.minipay.api.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class ClientThread implements Runnable {
    private final String hostName;
    private final int port;
    private final ObjectMapper jsonParser = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private List<Request> tasks = new LinkedList<>();
    private List<RequestResponsePair> results = new LinkedList<>();
    private final Thread thread;
    private volatile boolean isRunning;


    public ClientThread(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        thread = new Thread(this);
        isRunning = false;
    }

    public void addRequest(Request request) {
        if(isRunning) throw new IllegalStateException();
        tasks.add(request);
    }

    public List<RequestResponsePair> getResults() {
        if(isRunning) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public void sendRequests() {
        isRunning = true;
        thread.start();
    }

    private Response send(Request request) {
        Response response = null;

        try (
                Socket socket = new Socket(hostName, port);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()))
        ) {
            out.println(jsonParser.writeValueAsString(request));
            String responseStr = in.readLine();
            response = jsonParser.readValue(responseStr, Response.class);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return response;
    }

    @Override
    public void run() {
        for (Request request: tasks) {
            Response response = send(request);
            results.add(new RequestResponsePair(request, response));
        }
        isRunning = false;
    }
}
