package com.exam.computations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class FunctionCalculator extends UnicastRemoteObject implements ICalculator {
    public FunctionCalculator() throws RemoteException {
        super();
    }

    private double func(double a, double x){
        return a* Math.sin(x);
    }

    @Override
    public synchronized List<Double> compute(double a, double xMin, double xMax, double xDelta) {
        ArrayList<Double> results = new ArrayList<>();
        for(double x = xMin; x <= xMax; x += xDelta){
            results.add(func(a,x));
        }
        return results;
    }
}
