package com.exam.rmi_task;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

interface SolverInterface extends Remote {
    List<Double> compute(double a, double xMin, double xMax, double xDelta) throws RemoteException;
}
class FunctionSolver extends UnicastRemoteObject implements SolverInterface{
    public FunctionSolver() throws RemoteException {
        super();
    }

    @Override
    public synchronized List<Double> compute(double a, double xMin, double xMax, double xDelta) {
        ArrayList<Double> results = new ArrayList<>();
        for(double x = xMin; x <= xMax; x += xDelta){
            results.add(func(a,x));
        }
        return results;
    }

    private double func(double a, double x){
        return a* Math.sin(x);
    }
}
