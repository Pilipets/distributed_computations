package com.exam.socket_task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocketServer {
    private ServerSocket server = null;
    private Socket sock = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;
    private FunctionSolver functionSolver;

    public void start(int port) throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);
        functionSolver = new FunctionSolver();
        while (true) {
            sock = server.accept();
            objectInputStream = new ObjectInputStream(sock.getInputStream());
            objectOutputStream = new ObjectOutputStream(sock.getOutputStream());
            while (true) {
                processMessage();
            }
        }
    }

    private void processMessage() throws IOException, ClassNotFoundException {
        ComputeParams params = (ComputeParams) objectInputStream.readObject();
        objectOutputStream.writeObject(functionSolver.compute(
                params.a,
                params.xMin,params.xMax,
                params.xDelta)
        );
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MySocketServer server = new MySocketServer();
        server.start(2799);
    }
}