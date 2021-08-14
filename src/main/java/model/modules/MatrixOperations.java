package model.modules;

import model.Module;
import model.ModuleSet;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import utils.Pair;

import java.util.ArrayList;
import java.util.function.Function;

public class MatrixOperations implements ModuleSet {
    public static MatrixVariable matrixMultiplication(MatrixVariable a, MatrixVariable b) {
        if (a.colsNum() != b.rowsNum())
            throw new IllegalArgumentException("Matrix sizes are mismatched.");
        double[][] c = new double[a.rowsNum()][b.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < b.colsNum(); ++j)
                for (int k = 0; k < a.colsNum(); ++k)
                    c[i][j] += a.get(i, k) * b.get(k, j);
        return new MatrixVariable(c);
    }

    public static MatrixVariable matrixAddition(MatrixVariable a, MatrixVariable b) {
        if (a.colsNum() != b.colsNum() || a.rowsNum() != b.rowsNum())
            throw new IllegalArgumentException("Matrix sizes are mismatched.");
        double[][] c = new double[a.rowsNum()][a.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[i][j] = a.get(i, j) + b.get(i, j);
        return new MatrixVariable(c);
    }

    public static MatrixVariable matrixScalar(MatrixVariable a, NumericVariable b) {
        double[][] c = new double[a.rowsNum()][a.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[i][j] = a.get(i, j) * b.getValue();
        return new MatrixVariable(c);
    }

    public static MatrixVariable matrixScalar(NumericVariable b, MatrixVariable a) {
        return matrixScalar(a, b);
    }

    //rather as developer tool than something customer should use
    //could implement matrix scalar and alike
    public static MatrixVariable matrixApplyLambda(MatrixVariable a, Function<Double, Double> b) {
        double[][] c = new double[a.rowsNum()][a.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[i][j] = b.apply(a.get(i, j));
        return new MatrixVariable(c);
    }

    @Override
    public ArrayList<Pair<String, Module<?>>> getModules() {
        return null;
    }
}
