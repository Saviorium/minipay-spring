package ru.minipay.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerThreadPool extends Server {
    private static final int NTHREADS = 10;
    private final ExecutorService exec =
            new ThreadPoolExecutor(NTHREADS, NTHREADS, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    @Override
    void handleConnection(Socket connection) {
        exec.execute(new ServerWorker(connection));
    }

    private class ServerWorker implements Runnable{
        private final Socket connection;

        ServerWorker(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                ServerThreadPool.super.handleConnection(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
