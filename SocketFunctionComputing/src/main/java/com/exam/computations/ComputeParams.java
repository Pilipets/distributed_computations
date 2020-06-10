package com.exam.computations;

import java.io.Serializable;

public class ComputeParams implements Serializable {
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