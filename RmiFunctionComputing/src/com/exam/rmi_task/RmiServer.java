package com.exam.rmi_task;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class RmiServer {
    public static void main(String[] args) throws RemoteException {
        SolverInterface solver = new FunctionSolver();
        Registry registry = LocateRegistry.createRegistry(2799);
        registry.rebind("functionSolver",solver);
    }
}
