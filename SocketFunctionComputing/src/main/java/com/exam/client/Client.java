package com.exam.client;

import com.exam.computations.ComputeParams;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    private Socket sock;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Client(String ip, int port) throws IOException {
        sock = new Socket(ip, port);
        objectOutputStream = new ObjectOutputStream(sock.getOutputStream());
        objectInputStream = new ObjectInputStream(sock.getInputStream());
    }

    public List<Double> compute(double a, double xMin, double xMax, double xDelta) throws IOException, ClassNotFoundException {
        objectOutputStream.writeObject(new ComputeParams(
                a,
                xMin,xMax,
                xDelta));
        return (List<Double>)objectInputStream.readObject();
    }

    public void disconnect() throws IOException {
        sock.close();
    }
    public static void main(String[] args) {
        System.out.println("Computation of function: a*sin(x)");
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a coeficient: ");
        double a = in.nextDouble();
        System.out.print("Enter min x from range: ");
        double xMin = in.nextDouble();
        System.out.print("Enter max x from range: ");
        double xMax = in.nextDouble();
        System.out.print("Enter delta x: ");
        double xDelta = in.nextDouble();

        try {
            Client client = new Client("localhost",2799);
            List<Double> results = client.compute(a, xMin, xMax, xDelta);
            int idx = 0;
            for (double x = xMin; x <= xMax; x += xDelta) {
                System.out.println("[" + x + "|" + results.get(idx) + "]");
                idx++;
            }
            client.disconnect();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}