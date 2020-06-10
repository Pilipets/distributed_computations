package com.exam.rmi;

import com.exam.computations.FunctionCalculator;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer {
    public static void main(String[] args) throws RemoteException {
        FunctionCalculator calc = new FunctionCalculator();
        Registry registry = LocateRegistry.createRegistry(2799);
        registry.rebind("calculator",calc);
    }
}
