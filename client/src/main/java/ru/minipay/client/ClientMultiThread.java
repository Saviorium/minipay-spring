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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientMultiThread implements Runnable {
    private final String hostName;
    private final int port;
    private final ObjectMapper jsonParser = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private LinkedBlockingQueue<Request> tasks = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<RequestResponsePair> results = new LinkedBlockingQueue<>();
    private static final int NTHREADS = 10;
    private final Executor exec = Executors.newFixedThreadPool(NTHREADS);
    private volatile boolean isRunning = false;


    public ClientMultiThread(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public boolean addRequest(Request request) {
        return tasks.offer(request);
    }

    public RequestResponsePair getNextResult() throws InterruptedException {
        return results.take();
    }

    public void sendRequests() {
        isRunning = true;
        exec.execute(this);
    }

    public int getResultsNum() {
        return results.size();
    }

    public void shutdown() {
        isRunning = false;
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
        while (isRunning) {
            try {
                Request request = tasks.take();
                Response response = send(request);
                results.add(new RequestResponsePair(request, response));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
