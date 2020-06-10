package com.exam.computations;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ICalculator extends Remote {
    List<Double> compute(double a, double xMin, double xMax, double xDelta) throws RemoteException;
}
