package com.exam.server;

import com.exam.computations.ComputeParams;
import com.exam.computations.FunctionCalculator;
import com.exam.computations.ICalculator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    private ServerSocket server = null;
    private Socket sock = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;
    private ICalculator calculator;

    public void start(int port) throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);
        calculator = new FunctionCalculator();
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
        objectOutputStream.writeObject(calculator.compute(
                params.a,
                params.xMin,params.xMax,
                params.xDelta)
        );
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MyServer server = new MyServer();
        server.start(2799);
    }

}