package com.exam.socket_task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySocketServer {
    private ServerSocket server = null;
    private FunctionSolver functionSolver;

    public void start(int port) throws IOException {
        server = new ServerSocket(port);
        functionSolver = new FunctionSolver();

        final ExecutorService executor = Executors.newFixedThreadPool(6);
        while (true) {
            Socket listenerSocket = server.accept();
            executor.submit(()-> {
                try {
                    processMessage(listenerSocket);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void processMessage(final Socket listenerSocket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(listenerSocket.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(listenerSocket.getOutputStream());;

        ComputeParams params = (ComputeParams) objectInputStream.readObject();
        objectOutputStream.writeObject(functionSolver.compute(
                params.a,
                params.xMin,params.xMax,
                params.xDelta)
        );
    }

    public static void main(String[] args) throws IOException {
        MySocketServer server = new MySocketServer();
        server.start(2799);
    }
}