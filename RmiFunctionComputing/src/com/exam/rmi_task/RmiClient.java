package com.exam.rmi_task;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

class RmiClient {
    private SolverInterface functionSolver;

    public RmiClient(String remoteURL) throws RemoteException, NotBoundException, MalformedURLException {
        functionSolver = (SolverInterface) Naming.lookup(remoteURL);
    }

    public List<Double> compute(double a, double xMin, double xMax, double xDelta) throws RemoteException {
        return functionSolver.compute(a,xMin,xMax,xDelta);
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
            List<Double> results = new RmiClient("//127.0.0.1:2799/functionSolver").compute(
                    a,
                    xMin,xMax,
                    xDelta);
            int idx = 0;
            for (double x = xMin; x <= xMax; x += xDelta){
                System.out.println("[x=" + x + "|f(x)=" + results.get(idx) + "]");
                idx++;
            }
        } catch (RemoteException | NotBoundException |  MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
