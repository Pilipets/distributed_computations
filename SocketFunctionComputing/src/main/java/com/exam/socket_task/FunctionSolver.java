package com.exam.socket_task;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

class FunctionSolver {
    public List<Double> compute(double a, double xMin, double xMax, double xDelta) {
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

class ComputeParams implements Serializable {
    public double a;
    public double xMin;
    public double xMax;
    public double xDelta;

    public ComputeParams(double a, double xMin, double xMax, double xDelta) {
        this.a = a;
        this.xMin = xMin;
        this.xMax = xMax;
        this.xDelta = xDelta;
    }
}