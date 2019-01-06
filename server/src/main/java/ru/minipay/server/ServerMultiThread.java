package ru.minipay.server;

import java.io.IOException;
import java.net.Socket;

public class ServerMultiThread extends Server {
    @Override
    void handleConnection(Socket connection) {
        Thread t = new Thread(new ServerWorker(connection));
        t.start();
    }

    private class ServerWorker implements Runnable{
        private final Socket connection;

        ServerWorker(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                ServerMultiThread.super.handleConnection(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
