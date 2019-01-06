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
import java.util.concurrent.*;

public class ClientMultiThread {
    private final String hostName;
    private final int port;
    private final ObjectMapper jsonParser = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private LinkedBlockingQueue<RequestResponsePair> results = new LinkedBlockingQueue<>();
    private static final int NTHREADS = 10;
    private final ExecutorService exec = new ThreadPoolExecutor(NTHREADS, NTHREADS, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());


    public ClientMultiThread(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public void addRequest(Request request) {
        exec.execute(new ClientThread(request));
    }

    public RequestResponsePair getNextResult() throws InterruptedException {
        return results.take();
    }

    public void awaitTermination() throws InterruptedException {
        exec.awaitTermination(0L, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        exec.shutdown();
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

    private class ClientThread implements Runnable {
        private final Request request;

        private ClientThread(Request request) {
            this.request = request;
        }

        @Override
        public void run() {
            Response response = send(request);
            results.add(new RequestResponsePair(request, response));
        }
    }

}
